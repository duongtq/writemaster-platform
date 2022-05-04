import React from 'react'
import LoginRequire from './LoginRequire'
import UploadAvatarForm from './UploadAvatarForm';

const Profile = ({
	action,
	setAction
}
) => {
	const currentUser = JSON.parse(localStorage.getItem('currentUser')) || null
	// GET CURRENT USER FROM LOCAL STORAGE
	if (currentUser) {
		return (
			// <div className="container">
			// <div className="row">
			// 	<div className="col-lg-8 col-md-8 mx-auto my-auto">
			<div>
				{/* <h2 style={{ textAlign: 'center' }}>Profile</h2> */}

				<table className="table table-borderless">
					<thead>

					</thead>
					<tbody>
						<tr>
							<td>Avatar</td>
							<td><UploadAvatarForm /></td>
						</tr>
						{/* <tr>
							<td>User ID</td>
							<td>{currentUser.id}</td>
						</tr> */}
						<tr>
							<td>Username</td>
							<td>{currentUser.username}</td>
						</tr>
						<tr>
							<td>Email</td>
							<td>{ currentUser.email }</td>
						</tr>
						{/* <tr>
							<td>Authorities</td>
							<td>{currentUser.authorities.join(', ')}</td>
						</tr> */}
					</tbody>
				</table>

				{/* <UploadAvatarForm /> */}
			</div>
			// </div>
			// </div>
		)
	} else {
		return (
			<LoginRequire
				action={action}
				setAction={setAction}
			/>
		)
	}
}

export default Profile
