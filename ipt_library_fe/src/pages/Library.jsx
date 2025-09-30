import React, { useEffect, useState } from 'react';
import s from '../styles/Library.module.scss';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { toast } from 'react-toastify';

function Library({ books, afterDelete }) {
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [selected, setSelected] = useState(null); // lấy ra cuốn sách cần xóa

  const handleDeleteProduct = async (book) => {
    const apiUrl = `http://localhost:8080/books/${book.id}`;

    try {
      const response = await axios.delete(apiUrl);
      toast.success('Book deleted successfully!');
      afterDelete?.(); // reload lại trang sau khi xóa, check xem afterDelete có bị null hay undefined không xong thì mới thực hiện
    } catch (error) {
      toast.error('Failed to delete book. Please try again.');
      console.error('Error deleting book:', error.message);
    }
  };

  return (
    <div className={s.tableWrapper}>
      <h1 className={s.heading}>Library</h1>
      <table className={s.table} aria-label="Library books">
        <caption className={s.caption}>
          Books available in the library
          <Link to="/addbook">
            <button>Add book</button>
          </Link>
        </caption>
        <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>ISBN</th>
            <th>Author</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {books.map((book, index) => {
            return (
              <tr key={book.id}>
                <td>{book.id}</td>
                <td>{book.description}</td>
                <td>{book.isbn}</td>
                <td>{book.author}</td>
                <td>
                  <Link to={`view/${book.id}`}>
                    <button>View</button>
                  </Link>
                  <Link to={`edit/${book.id}`}>
                    <button>Edit</button>
                  </Link>
                  <button
                    type="button"
                    onClick={() => {
                      setSelected(book);
                      // lấy ra book cần delete
                      setConfirmOpen(true);
                    }}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>

      {confirmOpen && ( // && trả về giá trị falsy đầu tiên: confirmOpen true --> trả về vế sau
        <div className={s.confirm_delete}>
          <p>You want to delete "{selected?.description}" ?</p>
          <button className={s.btn_cancel} onClick={() => setConfirmOpen(false)}>
            Cancel
          </button>
          <button
            className={s.btn_delete}
            onClick={() => {
              handleDeleteProduct(selected);
              setConfirmOpen(false); // đóng popup sau khi xóa
            }}
          >
            Delete
          </button>
        </div>
      )}
    </div>
  );
}

export default Library;
