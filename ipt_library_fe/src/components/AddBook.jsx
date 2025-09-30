import React, { useEffect, useState } from 'react';
import s from '../styles/AddBook.module.scss';
import axios from 'axios';
import { toast } from 'react-toastify';
import { Link, useNavigate } from 'react-router-dom';

function AddBook() {
  const [title, setTitle] = useState('');
  const [isbn, setIsbn] = useState('');
  const [author, setAuthor] = useState('');
  const navigate = useNavigate();

  const handleAddBook = async () => {
    const apiUrl = 'http://localhost:8080/books';
    try {
      const response = await axios.post(apiUrl, {
        description: title,
        isbn: isbn,
        author: author,
      });
      console.log('Book added successfully:', response.data);
      toast.success('Book added successfully!');
      setTitle('');
      setIsbn('');
      setAuthor('');
      setTimeout(() => {
        navigate('/library');
      }, 2000);
    } catch (error) {
      toast.error('Failed to add book. Please try again.');
      console.error('Error adding book:', error.message);
    }
  };

  return (
    <>
      <Link to="/library">
        <button className={s.return}>Back to library</button>
      </Link>
      <div className={s.container}>
        <h2 className={s.title}>Add Book</h2>

        <form className={s.form}>
          <label className={s.label}>
            <span className={s.labelText}>Title</span>
            <input
              className={s.input}
              type="text"
              value={title}
              placeholder="Enter book title"
              onChange={(event) => setTitle(event.target.value)}
              required
            />
          </label>

          <label className={s.label}>
            <span className={s.labelText}>ISBN</span>
            <input
              className={s.input}
              type="text"
              value={isbn}
              placeholder="Enter ISBN"
              onChange={(event) => setIsbn(event.target.value)}
              required
            />
          </label>

          <label className={s.label}>
            <span className={s.labelText}>Author</span>
            <input
              className={s.input}
              type="text"
              value={author}
              placeholder="Enter author name"
              onChange={(event) => setAuthor(event.target.value)}
            />
          </label>

          <button className={s.button} type="button" onClick={() => handleAddBook()}>
            Add Book
          </button>
        </form>
      </div>
    </>
  );
}

export default AddBook;
