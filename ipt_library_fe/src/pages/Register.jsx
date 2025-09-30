import React, { useState } from 'react';
import s from '../styles/Register.module.scss';
import { Link } from 'react-router-dom';

function Register() {
  return (
    <div className={s.register_page}>
      <div className={s.return}>
        <Link to="/">
          <button>Back to home</button>
        </Link>
      </div>
      <div className={s.register_box}>
        <h2 className={s.title}>Create Account</h2>
        <form>
          <div className={s.form_group}>
            <label>Username</label>
            <input name="username" type="text" placeholder="Enter username" required />
          </div>

          <div className={s.form_group}>
            <label>Password</label>
            <input name="password" type="password" placeholder="Enter password" required />
          </div>

          <div className={s.form_group}>
            <label>Role</label>
            <select name="role" required>
              <option>User</option>
              <option>Manager</option>
            </select>
          </div>

          <button type="submit" className={s.btn_submit}>
            Register
          </button>
        </form>
      </div>
    </div>
  );
}

export default Register;
