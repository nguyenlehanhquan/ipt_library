import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import { MdHome } from 'react-icons/md';
import { RiLock2Fill } from 'react-icons/ri';
import { FaUser } from 'react-icons/fa';
import s from '../styles/Header.module.scss';
import logo from '../../public/logo.png';

export default function Header() {
  const navLinkClass = ({ isActive }) => (isActive ? `${s.link} ${s.active}` : s.link);

  return (
    <header className={s.header}>
      <div className={s.container}>
        <NavLink to="/" className={s.brand}>
          <img src={logo} alt="Logo" />
          <span>PHU THINH PRINTING</span>
        </NavLink>
        <nav className={s.nav}>
          <ul className={s.left}>
            <li>
              <NavLink to="/" className={navLinkClass} end>
                <MdHome className={s.icon} />
                Home
              </NavLink>
            </li>
            <li>
              <NavLink to="/library" className={navLinkClass}>
                Library
              </NavLink>
            </li>
          </ul>

          <ul className={s.right}>
            <li>
              <NavLink to="/login" className={navLinkClass}>
                <RiLock2Fill className={s.icon} />
                Login
              </NavLink>
            </li>
            <li>
              <NavLink to="/register" className={navLinkClass}>
                <FaUser className={s.icon} />
                Register
              </NavLink>
            </li>
          </ul>
        </nav>
      </div>
    </header>
  );
}
