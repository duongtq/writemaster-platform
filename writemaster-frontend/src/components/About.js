import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom';
import default_avatar from '../assets/default_avatar.png'

const About = () => {
  const [user, setUser] = useState({});

  const username = useParams().username;

  const token = JSON.parse(localStorage.getItem('currentUser')).token;

  useEffect(() => {
    console.log("in useEffect");
    axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/authors/${username}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then(res => {
      console.log(res.data);
      setUser(res.data);
    }).catch(err => {
      console.log(err.message);
    })
  }, [token, username]);

  // console.log('Authorities: ', user.authorities);

  return (
    // <div>
    //     <h2>User {user.id}</h2>
    //     <h2>{username}</h2>
    // </div>
    // <div className="container">
    <div className='row mt-4'>
      <div className='col-lg-8 col-md-6 mx-auto my-auto'>
        <div style={{ display: 'flex', flexDirection: 'column' }}>
          <div style={{ display: 'flex', justifyContent: 'center'}}>

            <div>
              <h2>About {user.realName}</h2>

              <div style={{width: '500px'}}>
                <p>{user.bio ? user.bio : 'Something about yourself'}</p>
              </div>
              
            </div>

            <div>
              <img style={{borderRadius: '25%'}} src={user.avatarUrl ? user.avatarUrl : default_avatar} alt="" width={'250px'} height={'250px'} />
            </div>


            {/* <table className="table table-bordered">
        <thead>

        </thead>
        <tbody>
          <tr>
            <td>User ID</td>
            <td>{user.id}</td>
          </tr>
          <tr>
            <td>Username</td>
            <td>{user.username}</td>
          </tr>
          <tr>
            <td>Real name</td>
            <td>{user.realName}</td>
          </tr>
        </tbody>
      </table> */}

            {/* <UploadAvatarForm /> */}
          </div>

          {/* <hr style={{width: 'max-content'}}/> */}
          {/* <Divider /> */}

          <div style={{ textAlign: 'left', display: 'flex', justifyContent: 'center' }}>
            <span>Number of followers&emsp;</span>- <span>&emsp;Number of following</span>
          </div>
        </div>
      </div>
    </div>
  )
}

export default About;
