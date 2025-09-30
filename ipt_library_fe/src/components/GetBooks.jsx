import React, { useEffect, useState } from 'react';
import Library from '../pages/Library.jsx';
import axios from 'axios';

function GetBooks() {
  const [books, setBooks] = useState([]);

  const getBooks = async () => {
    try {
      const apiUrl = 'http://localhost:8080/books';
      let response = (await axios.get(apiUrl)).data;
      console.log('Response: ', response.data);

      let cleanData = (response.data || []).map((b) => ({
        id: Number(b.id),
        description: String(b.description ?? ''), // ?? chỉ trả về '' nếu null hoặc undefined
        isbn: String(b.isbn ?? ''),
        author: String(b.author ?? ''),
      }));

      setBooks(cleanData);
    } catch (error) {
      console.error('Error: ', error.message);
    }
  };

  useEffect(() => {
    getBooks();
  }, []);

  return <Library books={books} afterDelete={getBooks} />;
}

export default GetBooks;
