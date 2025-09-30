import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import s from '../styles/View.module.scss';

function View(props) {
  const params = useParams();
  const bookId = params.bookId;
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [isbn, setIsbn] = useState('');
  const [author, setAuthor] = useState('');

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
  }, []);

  return (
    <div className={s.wrapper}>
      <div className={s.card}>
        <div className={s.header}>
          <h2 className={s.title}>Book Detail</h2>
          <span className={s.badge}>ID: {bookId}</span>
        </div>

        <div className={s.body}>
          <div className={s.row}>
            <span className={s.label}>Title</span>
            <span className={s.value}>{title || '—'}</span>
          </div>
          <div className={s.row}>
            <span className={s.label}>ISBN</span>
            <span className={s.value}>{isbn || '—'}</span>
          </div>
          <div className={s.row}>
            <span className={s.label}>Author</span>
            <span className={s.value}>{author || '—'}</span>
          </div>
        </div>

        <div className={s.actions}>
          <button type="button" className={s.btnPrimary} onClick={() => navigate('/library')}>
            Back
          </button>
          <button type="button" className={s.btnGhost} onClick={() => navigate(`/library/edit/${bookId}`)}>
            Edit
          </button>
        </div>
      </div>
    </div>
  );
}

export default View;
