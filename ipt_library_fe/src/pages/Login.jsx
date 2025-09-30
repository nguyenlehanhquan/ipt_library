import React from 'react';
import s from '../styles/Login.module.scss';
import { Link } from 'react-router-dom';

function SignIn(props) {
  return (
    <div className={s.login_page}>
      <div className={s.return}>
        <Link to="/">
          <button>Back to home</button>
        </Link>
      </div>
      <div className={s.login_box}>
        <h2 className={s.title}>Login</h2>
        <form>
          <div className={s.form_group}>
            <label>Username</label>
            <input type="username" placeholder="Enter your username" />
          </div>
          <div className={s.form_group}>
            <label>Password</label>
            <input type="password" placeholder="Enter your password" />
          </div>
          <button type="submit" className={s.btn_login}>
            Sign In
          </button>
        </form>
      </div>
    </div>
  );
}

export default SignIn;
