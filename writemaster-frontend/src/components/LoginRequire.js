/* eslint-disable react/prop-types */
import React from 'react'
import Login from './Login'

const LoginRequire = ({
	action,
	setAction
}) => {
	return (
		<div style={{ textAlign: 'center' }}>
			<h2>You must login first.</h2>
			<Login
				action={action}
				setAction={setAction}
			/>
		</div>
	)
}

export default LoginRequire
