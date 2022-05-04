import React, { useState, useEffect } from 'react'
import Header from './components/Header';
import 'bootstrap';
import './App.css';

const App = () => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;

  const [postList, setPostList] = useState([]);

  const [action, setAction] = useState(currentUser ? "LOGOUT" : "LOGIN");

  // const removeAntDesignEffect = () => {
  //   const body = document.getElementsByTagName("body")[0];
  //   body.classList.remove("ant-scrolling-effect");
  //   // body.style.removeProperty('width');
  //   body.style.removeProperty('width');
  //   // body.style.overflow = 'auto';
  // }

  // useEffect(() => {
  //   removeAntDesignEffect();
  // });

  return (
    // <div className="container">
    <>
      <Header postList={postList} setPostList={setPostList} 
        action={action}
        setAction={setAction} />
    </>
  );
}

export default App;
