import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import './main.css';
import App from './App.jsx';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Home from './pages/Home.jsx';
import 'bootstrap/dist/css/bootstrap.min.css';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import GetBooks from './components/GetBooks.jsx';
import AddBook from './components/AddBook.jsx';
import View from './components/View.jsx';
import UpdateBook from './components/UpdateBook.jsx';

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          <Route index element={<Home />} />
          <Route path="/library" element={<GetBooks />} />
          <Route path="/addbook" element={<AddBook />} />
          <Route path="/library/view/:bookId" element={<View />} />
          <Route path="/library/edit/:bookId" element={<UpdateBook />} />
        </Route>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
      </Routes>
    </BrowserRouter>
  </StrictMode>,
);
