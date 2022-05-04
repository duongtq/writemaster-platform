import React, { useState } from 'react';
import { BrowserRouter, Switch, Route, Link, Redirect, useHistory } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import writemaster from '../assets/writemaster.png';
import Login from './Login';
import Homepage from './Homepage';
import PostList from './post/PostList';
import PostDetail from './post/PostDetail';
import PostEdit from './post/EditPost';
import Signup from './Signup';
import About from './About';
import UserPage from './UserPage';
// import socket from '../socket-io/Socket';
import { Menu, Dropdown, Button } from 'antd';
import Notification from './Notification';
import SocialPage from './social/SocialPage';
import BookmarkList from './bookmark/BookmarkList';
import CreatePostForm from './post/CreatePost';
import Profile from './Profile';

const Header = ({
	postList,
	setPostList,
	action,
	setAction
}) => {
	
	const currentUser = JSON.parse(localStorage.getItem('currentUser'));

	// const history = useHistory();

	// const menu = (
	// 	<Menu>
	// 		<Menu.Item>
	// 			{/* <a target="_blank" rel="noopener noreferrer" href="https://www.antgroup.com">
	// 				1st menu item
	// 			</a> */}
	// 			<Link to='abc'>
	// 				To write a post
	// 			</Link>
	// 		</Menu.Item>
	// 		<Menu.Item>
	// 			{/* <a target="_blank" rel="noopener noreferrer" href="https://www.aliyun.com">
	// 				2nd menu item
	// 			</a> */}
	// 			<Link to='abc'>
	// 				To chat
	// 			</Link>
	// 		</Menu.Item>
	// 		<Menu.Item>
	// 			{/* <a target="_blank" rel="noopener noreferrer" href="https://www.luohanacademy.com">
	// 				3rd menu item
	// 			</a> */}
	// 			<Link to='abc'>
	// 				To settings
	// 			</Link>
	// 		</Menu.Item>
	// 	</Menu>
	// );

	const onLoginLogoutHandler = () => {
		if (action === "LOGOUT") {
			setAction(action => "LOGIN");

			// TODO: emit the event that user logged out
			// socket.emit('user-logged-out', {
			// 	id: currentUser.id,
			// 	username: currentUser.username
			// });

			const userLogoutPayload = {
				channel: 'user-logged-out',
				message: {
					id: currentUser.id,
					username: currentUser.username
				}
			}

			localStorage.clear();
			setPostList(postList => []);
		}
	}

	const renderLoginLogout = () => {
		if (action === "LOGIN") {


			return <Login
				action={action}
				setAction={setAction} />
		} else {
			return (

				<PostList
					postList={postList}
					setPostList={setPostList}
					action={action}
					setAction={setAction}
				/>
			)
		}
	}

	return (
		<>
			<BrowserRouter>
				<header>
					<div className='nav-header'>
						<nav>
							<Link to="/">
								<img src={writemaster} alt="" />
							</Link>

							<ul className="nav-list">
								<li>
									<Link to="/" style={{ textDecoration: 'none' }} >HOME</Link>
								</li>

								<li>
									<Link to="/content" style={{ textDecoration: 'none' }}>CONTENT</Link>
								</li>

								{/* TEST */}
								{/* <li> */}
								{/* <Dropdown
										overlay={menu}
										placement='bottomCenter'
										trigger={['click']}
									>
										null
									</Dropdown> */}
								{/* </li> */}

								<li>
									<Link to="/userPage/profile"
										style={{ textDecoration: 'none', width: '6em', textAlign: 'left' }}>
										{/* TODO: make this a drowdown when user logs in. */}
										{currentUser !== null ? currentUser.username : 'PROFILE'}
										{/* {currentUser !== null
											? (<Dropdown
												overlay={menu}
												placement='bottomCenter'
												trigger={['click']}
											>
												currentUser.username
											</Dropdown>)
											: 'PROFILE'} */}

									</Link>
								</li>

								<li id="login-logout" onClick={onLoginLogoutHandler} >
									<Link to="/login" style={{ textDecoration: 'none' }}>{action}</Link>
								</li>

							</ul>
						</nav>
					</div>
				</header>

				<Switch>
					<Route path="/signup">
						<Signup />
					</Route>

					<Route path="/homepage">
						<Homepage />
					</Route>
					<Route path="/content">
						<PostList
							postList={postList}
							setPostList={setPostList}
							action={action}
							setAction={setAction}
						/>

					</Route>
					<Route path="/userPage">
						{/* <Profile
						action={action}
						setAction={setAction}
					/> */}
						<UserPage
							action={action}
							setAction={setAction}
							postList={postList}
							setPostList={setPostList}
						/>
					</Route>
					<Route path="/login">
						{renderLoginLogout()}
					</Route>
					<Route path="/postdetail/:id">
						<PostDetail
							postList={postList}
							setPostList={setPostList}
						/>
					</Route>
					<Route path="/postedit/:id">
						<PostEdit
							postList={postList}
							setPostList={setPostList} />
					</Route>
					<Route exact path="/">
						<Redirect to="/homepage" />
					</Route>

					<Route path='/about/:username'>
						<About />
					</Route>

				</Switch>
			</BrowserRouter>
		</>
	);
}


export default Header;

