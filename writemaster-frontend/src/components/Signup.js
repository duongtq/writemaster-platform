import React from 'react'
import { useHistory } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import axios from 'axios'
import Helper from '../utils/Helper'

const Signup = () => {
	const history = useHistory();

	return (
		<Formik
			initialValues={{ firstName: '', lastName: '', birthDate: Helper.getDefaultDateTime(), username: '', password: '', email: '', authorities: [] }}
			validate={values => {
				const errors = {
					
				}
				if (!values.firstName) {
					errors.firstName = 'First name should not be empty'
				} 

				if (!values.lastName) {
					errors.lastName = 'Last name should not be empty'
				}
				if (!values.username) {
					errors.username = 'Username should not be empty'
				}
				if (!values.password) {
					errors.password = 'Password should not be empty'
				} else if (values.password.length < 8) {
					errors.password = 'Password length must be at least 8 characters'
				}

				if (!values.email) {
					errors.email = 'Email should not be empty'
				} else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(values.email)) {
					errors.email = 'Invalid email address'
				}

				return errors
			}}

			onSubmit={(values, { setSubmitting }) => {
				const payload = {
					firstName: values.firstName,
					lastName: values.lastName,
					username: values.username,
					password: values.password,
					email: values.email,
					birthDate: values.birthDate,
					createdDate: Helper.getDefaultDateTime(),
					authorities: ['AUTHOR']
				}

				console.log('payload: ', payload);

				axios.post(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/authors/register`, payload)
					.then(response => {
						switch (response.status) {
							case 201:
								window.confirm('Signed up successfully.')
								setTimeout(() => {
									history.push('/content')
								}, 1500)
								break
							case 401:
								window.confirm('Invalid information')
								break
							default:
								window.confirm('Server error. Try again later.')
								break
						}
					}).catch(err => {
						if (err.response.data.message.includes('Author')) {
							document.getElementById('duplicateUsername').textContent = 'That username is taken. Try another.'
							setTimeout(() => {
								document.getElementById('duplicateUsername').textContent = '&nbsp;'
							}, 2500)
						} else if (err.response.data.message.includes('Email')){
							document.getElementById('duplicateEmail').textContent = 'That email is taken. Try another.'
							setTimeout(() => {
								document.getElementById('duplicateEmail').textContent = '&nbsp;'
							}, 2500)
						}
					})
			}}
		>
			{({ isSubmitting }) => (

				// <div className="container mt-3 mb-3">
					<div className="row">
						<div className="col-lg-8 col-md-8 mx-auto my-auto">
							<p style={{ textAlign: 'center', fontSize: '4em' }}>Create your account</p>
							<Form>
								<div className="form-group">
									<label htmlFor="firstName">First Name</label><br />
									<Field
										className="form-control"
										id="firstName"
										component="input"
										type="text"
										name="firstName"
										placeholder='First Name'
									/>

									<ErrorMessage
										name="firstName"
										component='span'
										// style={{ color: 'red', marginTop: '4px', marginBottom: '4pd' }}
										style={{ color: 'red' }}
									/>
								</div>

								<div className="form-group">
									<label htmlFor="lastName">Last Name</label><br />
									<Field
										className="form-control"
										id="lastName"
										component="input"
										type="text"
										name="lastName"
										placeholder='Last Name'
									/>
									<ErrorMessage
										name="lastName"
										component="div"
										style={{ color: 'red' }}
									/>
								</div>

								<div className="form-group">
									<label htmlFor="username">Username</label><br />
									<Field
										className="form-control"
										id='username'
										component="input"
										type="text"
										name="username"
										placeholder='Username'
									/>
									<ErrorMessage
										name="username"
										component="div"
										style={{ color: 'red' }}
									/>
									<div>
										<span id="duplicateUsername" style={{ color: 'red' }}></span>
									</div>
								</div>

								<div className="form-group">
									<label htmlFor="password">Password</label><br />
									<Field
										className="form-control"
										id="password"
										component="input"
										type="password"
										name="password"
									/>
									<ErrorMessage
										name="password"
										component="div"
										style={{ color: 'red' }}
									/>
								</div>

								<div className="form-group">
									<label htmlFor="email">Email</label><br />
									<Field
										className="form-control"
										id="email"
										component="input"
										type="email"
										name="email"
									/>
									<ErrorMessage
										name="email"
										component="div"
										style={{ color: 'red' }}
									/>
									<div>
										<span id="duplicateEmail" style={{ color: 'red' }}></span>
									</div>
								</div>

								<div className="form-group">
									<label htmlFor="birthDate">Birth Date</label><br />
									<Field
										className="form-control"
										id='birthDate'
										component='input'
										type='datetime-local'
										name='birthDate'
									/>
								</div>

								<div className="mt-4 mb-4">

								</div>

								<div className="form-group">
									<button
										className="btn btn-warning btn-small form-control shadow-none"
										type="submit"
									>
										SIGN UP
									</button>
								</div>

							</Form>
						</div>

					</div>
				// </div>
			)}
		</Formik>
	)
}

export default Signup
