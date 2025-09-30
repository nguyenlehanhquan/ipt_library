import React, { useEffect, useState } from 'react';
import s from '../styles/UpdateBook.module.scss';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';

function UpdateBook(props) {
  const params = useParams(); // lấy giá trị đó ở trên đường link /edit/:bookId
  const bookId = params.bookId; // set book id mà cần lấy ra = với book id lấy từ param

  const [title, setTitle] = useState('');
  const [isbn, setIsbn] = useState('');
  const [author, setAuthor] = useState('');
  const navigate = useNavigate();

  const getBookById = async () => {
    const apiUrl = `http://localhost:8080/books/${bookId}`;
    try {
      let response = (await axios.get(apiUrl)).data;
      setTitle(response.data ? response.data.description : '');
      setIsbn(response.data ? response.data.isbn : '');
      setAuthor(response.data ? response.data.author : '');
    } catch (error) {
      console.error('Error getting book:', error.message);
    }
  };

  useEffect(() => {
    getBookById();
  }); // mount component lần đầu

  const handleUpdateBook = async () => {
    const apiUrl = `http://localhost:8080/books/${bookId}`;
    try {
      const response = await axios.put(apiUrl, {
        description: title,
        isbn: isbn,
        author: author,
      });
      console.log('Book updated successfully:', response.data);
      setTitle('');
      setIsbn('');
      setAuthor('');
      toast.success('Book updated successfully!');
      const t = setTimeout(() => {
        navigate('/library');
      }, 2000);
      return () => clearTimeout(t);
    } catch (error) {
      console.error('Error updating book:', error.message);
    }
  };

  return (
    <div className={s.container}>
      <h2 className={s.title}>Update Book</h2>
      <form className={s.form}>
        <label className={s.label}>
          <span>Title</span>
          <input
            className={s.input}
            type="text"
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            placeholder="Enter book title"
          />
        </label>

        <label className={s.label}>
          <span>ISBN</span>
          <input
            className={s.input}
            type="text"
            value={isbn}
            onChange={(event) => setIsbn(event.target.value)}
            placeholder="Enter ISBN"
          />
        </label>

        <label className={s.label}>
          <span>Author</span>
          <input
            className={s.input}
            type="text"
            value={author}
            onChange={(event) => setAuthor(event.target.value)}
            placeholder="Enter author"
          />
        </label>

        <div className={s.actions}>
          <button className={s.btnCancel} type="button" onClick={() => navigate('/library')}>
            Cancel
          </button>
          <button className={s.btnSave} type="button" onClick={() => handleUpdateBook()}>
            Update
          </button>
        </div>
      </form>
    </div>
  );
}

export default UpdateBook;
