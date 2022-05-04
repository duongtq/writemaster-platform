import React from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'

const LoginImproved = () => {
	const formik = useFormik({
		initialValues: {
			username: '',
			password: ''
		},
		validationSchema: Yup.object({
			username: Yup.string()
				.required('Required'),
			password: Yup.string()
				.required('Required')
		}),
		onSubmit: values => {
			alert(JSON.stringify(values, null, 2))
		}
	})
	return (
		<form onSubmit={formik.handleSubmit}>
			{/* <label htmlFor="firstName">First Name</label> */}
			<input
				placeholder="Username"
				id="username"
				name="username"
				type='text'
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.username}
			/>

			{formik.touched.username && formik.errors.username
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.username}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}

			{/* <label htmlFor="lastName">Last Name</label> */}
			<input
				placeholder="Password"
				id="password"
				name="password"
				type='password'
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.password}
			/>
			{formik.touched.password && formik.errors.password
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.password}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}

			{/* <label htmlFor="email">Email Address</label> */}
			{/* <input
				placeholder="Email"
				id="email"
				name="email"
				type="search"
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.email}
			/>
			{formik.touched.email && formik.errors.email
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.email}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)} */}

			<button type="submit">Submit</button>
		</form>
	)
}

export default LoginImproved;
