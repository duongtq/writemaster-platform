import React from 'react'
import Helper from '../../utils/Helper'
import 'bootstrap/dist/css/bootstrap.css'
import { Link } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faEllipsisV } from '@fortawesome/free-solid-svg-icons'
import { faEdit } from '@fortawesome/free-solid-svg-icons'
import { faTrash } from '@fortawesome/free-solid-svg-icons'
import { faFlag } from '@fortawesome/free-solid-svg-icons'
import { Menu, Dropdown, Button, Modal } from 'antd';
import { useState } from 'react';
import axios from 'axios';

const Post = ({ data, postList, setPostList }) => {
	const currentUser = JSON.parse(localStorage.getItem('currentUser'))

	const removeAntDesignEffect = () => {
		const body = document.getElementsByTagName("body")[0];
		body.classList.remove("ant-scrolling-effect");
		body.style.removeProperty('width');
		// body.style.setProperty('width', 'calc(100% + 15px)');
		// body.style.overflowX = 'hidden';
		body.style.overflowY = 'scroll';
	}

	const post = {
		id: data.id,
		author: data.author,
		title: data.title,
		description: data.description,
		content: data.content,
		createdDate: data.createdDate,
		authorId: data.authorId,
		info: `By ${data.author} - Originally posted on ${Helper.formatCreatedDate(data.createdDate)}`,
		numberOfLikes: data.numberOfLikes,
		numberOfComments: data.numberOfComments,
		numberOfShares: data.numberOfShares,
		comments: data.comments
	}

	const menu = (
		<Menu
			style={{
				width: '150px'
			}}
		>
			{currentUser.id === post.authorId
				? <><Menu.Item
					key='1'
				>
					<Link
						to={`/postedit/${post.id}`}>
						<FontAwesomeIcon icon={faEdit} /><span className='ml-2'>Edit</span>
					</Link>
				</Menu.Item>
					<Menu.Item
						key='2'
					>
						{/* <Link
							to={`/postedit/${post.id}`}> */}
						<div onClick={() => showModal()}>
							<FontAwesomeIcon icon={faTrash} />
							<span className='ml-2' >&nbsp;Delete</span>
						</div>

						{/* </Link> */}
					</Menu.Item></>
				: <Menu.Item
					key='1'
				>
					<Link
						to="google.com">
						<FontAwesomeIcon icon={faFlag} /><span className='ml-2'>Report</span>
					</Link>
				</Menu.Item>
			}
		</Menu>
	);

	const [deletePostModal, setDeletePostModal] = useState(false);

	const showModal = () => {
		setTimeout(() => {
			removeAntDesignEffect();

		}, 0);
		setDeletePostModal(true);
	};

	const handleOk = async () => {
		try {
			const deletePostResult = await axios.delete(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/${post.id}`, {
				headers: {
					Authorization: `Bearer ${currentUser.token}`
				}
			});

			console.log('delete post result: ', deletePostResult);

		} catch (err) {
			console.log(err);
		}

		try {
			const getAllPost = await axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/`, {
				headers: {
					Authorization: `Bearer ${currentUser.token}`
				}
			});
			setPostList(getAllPost.data);
			localStorage.setItem('postList', JSON.stringify(getAllPost.data));
		} catch (err) {
			console.log(err);
		}

		setDeletePostModal(false);
	};

	const handleCancel = () => {
		setDeletePostModal(false);
	};


	return (
		<div className='mt-4 mb-4'>

			<Modal
				visible={deletePostModal}
				title="Delete post"
				onOk={handleOk}
				onCancel={handleCancel}
				footer={[
					<Button
						key="back"
						onClick={handleCancel}
						style={{
							width: '100px',
							height: '40px'
						}}>
						Cancel
					</Button>,
					<Button
						key='submit'
						type='primary'
						onClick={handleOk}
						style={{
							width: '100px',
							height: '40px'
						}}>
						Submit
					</Button>,
				]}
			>
				<h4>{post.title}</h4>
				<p className="post-info">{post.info}</p>
				<p>{post.description}</p>
			</Modal>

			<article className='single-post'>
				<div className="title-action">
					<Link to={`/postdetail/${post.id}`} style={{ textDecoration: 'none' }}>
						<h2>{post.title}</h2>
					</Link>

					<Dropdown
						overlay={menu}
						placement='bottomCenter'
						trigger={['click']}
						arrow
					>
						<button
							className='post-action'
						>
							<FontAwesomeIcon icon={faEllipsisV} />
						</button>
					</Dropdown>

				</div>

				{/* <h3>{post.description}</h3> */}
				<p className="post-info">{post.info}</p>
			</article>

		</div>
	)
}

export default Post
