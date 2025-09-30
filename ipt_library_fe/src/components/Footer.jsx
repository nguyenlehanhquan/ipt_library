import React from 'react';
import s from '../styles/Footer.module.scss';
import { FaCopyright } from 'react-icons/fa';

function Footer(props) {
  return (
    <footer className={s.footer}>
      <div className={s.container}>
        <div className={s.info}>
          <h5>
            <FaCopyright className={s.copyright} />
            opyright 2025 . Phu Thinh Printing & Trading Services Co., Ltd
          </h5>
          <p>
            Address: Lot B2-2-5, Nam Thang Long industrial zone, Thuy Phuong ward, Bac Tu Liem district, Hanoi, Vietnam
          </p>
          <p>Điện thoại: (024) 3763 5590</p>
          <p>Email: ipt@inphuthinh.vn</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
