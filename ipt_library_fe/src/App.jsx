import './App.module.scss';
import Header from './components/Header.jsx';
import { Outlet } from 'react-router-dom';
import Footer from './components/Footer.jsx';
import s from './App.module.scss';
import { Bounce, ToastContainer } from 'react-toastify';

function App() {
  return (
    <div className={s.app}>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="colored"
        transition={Bounce}
      />
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
}

export default App;
