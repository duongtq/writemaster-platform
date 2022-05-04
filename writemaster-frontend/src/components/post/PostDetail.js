import React, { useEffect } from 'react'
import { Link, useParams } from 'react-router-dom'

import Interaction from './Interaction'

import DOMPurify from "dompurify";

const PostDetail = ({ postList, setPostList }) => {
	const id = parseInt(useParams().id)

	// let post = postList.find(ele => ele.id === id);
	let post;

	// console.log('PostDetail: post list ', postList);

	// postList.forEach(ele => {
	// 	console.log(ele);
	// 	console.log(typeof(ele));
	// })
	// console.log('Post ID: ', id);

	// useEffect(() => {
	// 	return () => {
	// 		const body = document.getElementsByTagName('body')[0];
	// 		body.style.setProperty('width', 'calc(100% - 15px)');
	// 		console.log('GOT HERE');
	// 	}
	// });

	const removeAntDesignEffect = () => {
		const body = document.getElementsByTagName("body")[0];
		// const html = document.getElementsByTagName("html")[0];
		body.classList.remove("ant-scrolling-effect");
		// body.style.removeProperty('width');
		// body.style.removeProperty('width');
		body.style.overflowY = 'scroll';
	}

	useEffect(() => {
		removeAntDesignEffect();
	});

	if (postList.length !== 0) {
		// console.log(postList);
		post = postList.find(ele => ele.id === id)
		// console.log('PostDetail: post ', post);

		// console.log('post from curren postList');
	} else {
		// let postList = ;
		post = JSON.parse(localStorage.getItem('postList')).find(ele => ele.id === id)
		// console.log("post from localStorage");
		// console.log('PostDetail: ', post);
	}

	return (
		<div className='container mt-2 mb-2'>
			<div className='row mt-2 mb-2'>
				<div className='col-lg-10 col-md-8 mx-auto my-auto'>
					{/* <div> */}
					<ul className='author-nav'>
						<li>
							<Link to={'/profile'} style={{ textDecoration: 'none', color: 'black' }}>
								<div>
									<span id='author-name' style={{ fontSize: '2em', fontWeight: 'bold' }}>{post.authorName}</span>
								</div>
							</Link>
						</li>
						<li>
							<div id='number-of-followers'>
								<span>100 followers</span>
							</div>
						</li>
						<li>
							<Link to={`/about/${post.author}`} style={{ textDecoration: 'none', color: 'black' }}>
								<div>
									<span style={{ opacity: '0.4' }}>About</span>
								</div>
							</Link>
						</li>
						<li>
							<button id='follow-button' className='btn btn-warning shadow-none ml-2'>
								Follow
							</button>
						</li>
					</ul>
					{/* </div> */}
					{/* <div> */}
					<h2 style={{ fontSize: '50px', textAlign: 'center' }}>{post.title}</h2>
					{/* <h3 style={{fontSize: '40px'}}>{post.description}</h3> */}
					{/* <div> */}
					{/* <div className='mt-4' dangerouslySetInnerHTML={{ __html: post.content }}> */}

					{/* <div className='clearfix'>

					</div> */}

					{/* <div className='clearfix> */}
					<div className='clearfix' dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(post.content) }}>
						{/* {DOMPurify.sanitize(post.content)} */}

					</div>

					{/* <div className='clearfix'>
						{DOMPurify.sanitize(post.content)}
					</div> */}
					{/* </div> */}
					{/* {post.content} */}
					{/* </div> */}

					{/* <hr /> */}

					

					<Interaction post={post} postList={postList} setPostList={setPostList} />

				</div>
			</div >
		</div >
	)
}

export default PostDetail
