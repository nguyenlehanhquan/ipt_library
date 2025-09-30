import React, { useState } from 'react';
import s from '../styles/Home.module.scss';
import { Carousel } from 'react-bootstrap';
import first_slide from '../assets/pressm4.jpg';
import second_slide from '../assets/pressm6.jpg';
import third_slide from '../assets/pressm6_zoom.jpg';

function Home(props) {
  const [index, setIndex] = useState(0);

  const handleSelect = (selectedIndex) => {
    setIndex(selectedIndex);
  };

  return (
    <div className={s.home}>
      <Carousel activeIndex={index} onSelect={handleSelect}>
        <Carousel.Item className={s.carousel_item}>
          <img src={first_slide} alt="first_slide" />
          <Carousel.Caption className={s.carousel_caption}>
            <h2>Highly trained operators</h2>
            <p>Our printing presses are touched by the best and most experienced operators</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item className={s.carousel_item}>
          <img src={second_slide} alt="second_slide" />
          <Carousel.Caption className={s.carousel_caption}>
            <h2>Relentless upgrades and innovation</h2>
            <p>We seek highest quality possible with most cutting-edge technologies</p>
          </Carousel.Caption>
        </Carousel.Item>
        <Carousel.Item className={s.carousel_item}>
          <img src={third_slide} alt="third_slide" />
          <Carousel.Caption className={s.carousel_caption}>
            <h2>Listening to inspire</h2>
            <p>We listen to understand and make our inks color your hearts</p>
          </Carousel.Caption>
        </Carousel.Item>
      </Carousel>
    </div>
  );
}

export default Home;
