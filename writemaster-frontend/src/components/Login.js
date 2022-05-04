import React from 'react';
import { useState, useEffect, Fragment } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import axios from 'axios'
import { Link } from 'react-router-dom';

const Login = ({
  action,
  setAction
}) => {
  
  return (

    <Fragment>
      <div className="clearfix"></div>
      <Formik
        initialValues={{ username: '', password: '' }}
        validate={values => {
          const errors = {}
          if (!values.username) {
            errors.username = <span style={{ color: 'red' }}>  Required</span>
          }
          if (!values.password) {
            errors.password = <span style={{ color: 'red' }}>  Required</span>
          }
        }}

        onSubmit={(values, { setSubmitting }) => {
          values.wrongCredential = false
          console.log('Submitted')

          axios.post(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/authenticate`, {
            username: values.username,
            password: values.password
          })
            .then(response => {
              if (response.data.userId) {
                const currentUser = {
                  id: response.data.userId,
                  username: response.data.username,
                  email: response.data.email,
                  authorities: response.data.authorities,
                  token: response.data.jwtToken,
                  avatarUrl: response.data.avatarUrl
                }
                localStorage.setItem('currentUser', JSON.stringify(currentUser))

                console.log('current avatar url: ', currentUser.avatarUrl);

                // GET PUBLIC_ID
                if (currentUser.avatarUrl) {
                  const xxx = currentUser.avatarUrl.split('/');
                  // console.log('xxx: ', xxx);
                  const yyy = xxx[xxx.length - 1].split('.');
                  // console.log('yyy: ', yyy);

                  const public_id = `${xxx[xxx.length - 2]}/${yyy[0]}`;
                  // console.log('currentPublicId: ', public_id);

                  localStorage.setItem('currentPublicId', public_id);
                }

                const onlineUser = {
                  id: currentUser.id,
                  username: currentUser.username
                }

                const userLoginPayload = {
                  channel: 'user-logged-in',
                  message: onlineUser
                }

                setAction(action => 'LOGOUT')
              }
            })
            .catch(err => {
              console.log('Error login: ', err.message)
              document.getElementById('wrongCredential').textContent = 'Incorrect username/password. Please try again.'

              setTimeout(() => {
                if (document.getElementById('wrongCredential')) {
                  document.getElementById('wrongCredential').textContent = ''
                }
              }, 2500)
            })
        }}
      >
        {({ isSubmitting }) => (
          // <div className="container">
          //   <div className="row">
          <div className="login-form col-lg-8 col-md-6 mx-auto my-auto">
            {/* <div> */}
            <Form>
              <div className="form-group">
                <Field className="mt-2 form-control" type="text" name="username" placeholder="Username" />
                <ErrorMessage name="username" component="div" />
              </div>

              <div className="form-group">
                <Field className="form-control" type="password" name="password" placeholder="Password" />
                <ErrorMessage name="password" component="div" />
              </div>

              <div style={{ textAlign: 'center' }}>
                <span id="wrongCredential" style={{ color: 'red' }}>&nbsp;</span><br />
              </div>

              <div className="form-group">
                <button className="btn btn-primary shadow-none form-control" type="submit">LOGIN</button>
              </div>
            </Form>

            <p style={{ textAlign: 'center' }}>Don't have an account ?
              <Link to='/signup'> Sign up here
              </Link>
            </p>
          </div>
        )}
      </Formik>
    </Fragment>
  )
}

export default Login
