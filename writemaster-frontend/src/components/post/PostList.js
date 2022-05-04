import React, { useState, useEffect } from 'react';
import Post from './Post';
import LoginRequire from '../LoginRequire';
import axios from 'axios';
import { List } from 'antd';

const PostList = ({
	postList,
	setPostList,
	action,
	setAction
}) => {

	const [searchTerm, setSearchTerm] = useState('')

	// postList.forEach(ele => {
	//     console.log(ele);
	// })
	const [currentPage, setCurrentPage] = useState(parseInt(localStorage.getItem('currentPage')) || 1);

	const [author, setAuthor] = useState('')
	const [filter, setFilter] = useState(0)
	// 0: none
	// 1: title asc (A-Z)
	// 2: title desc (Z-A)
	// 3: date asc (oldest -> latest)
	// 4: date desc (latest -> oldest)

	const currentUser = JSON.parse(localStorage.getItem('currentUser')) || null


	const removeAntDesignEffect = () => {
		const body = document.getElementsByTagName("body")[0];
		body.classList.remove("ant-scrolling-effect");
		body.style.removeProperty('width');
		// body.style.setProperty('width', 'calc(100% + 15px)');
		// body.style.overflowX = 'hidden';
		body.style.overflowY = 'scroll';
	}

	useEffect(() => {
		removeAntDesignEffect();

		// GET CURRENT USER FROM LOCAL STORAGE
		if (currentUser && currentUser.token && postList.length === 0) {
			axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts`, {
				headers: {
					Authorization: `Bearer ${currentUser.token}`
				}
			})
				.then(response => {
					setPostList(response.data)
					localStorage.setItem('postList', JSON.stringify(response.data))
				})
				.catch(err => {
					console.log('Error fetching data from server.')
				})
		}

		// return () => {
		// 	console.log('PostList unmounted');
		// 	localStorage.setItem('currentPage', 1);
		// }

		// setCurrentPage(1);
	})

	const changeFilterType = () => {
		const filterType = parseInt(document.getElementById('filter').value)
		setFilter(filter => filterType)
	}

	const filterPostList = (filterType) => {
		if (filterType === 0) {
			return postList.sort((post1, post2) => {
				if (post1.id < post2.id) return -1
				if (post1.id > post2.id) return 1
				return 0
			})
		}

		if (filterType === 1) {
			return postList.sort((post1, post2) => {
				if (post1.title < post2.title) return -1
				if (post1.title > post2.title) return 1
				return 0
			})
		}

		if (filterType === 2) {
			return postList.sort((post1, post2) => {
				if (post1.title > post2.title) return -1
				if (post1.title < post2.title) return 1
				return 0
			})
		}

		if (filterType === 3) {
			return postList.sort((post1, post2) => {
				const date1 = new Date(post1.createdDate)
				const date2 = new Date(post2.createdDate)

				if (date1 < date2) return -1
				if (date1 > date2) return 1
				return 0
			})
		}

		if (filterType === 4) {
			return postList.sort((post1, post2) => {
				const date1 = new Date(post1.createdDate)
				const date2 = new Date(post2.createdDate)

				if (date1 > date2) return -1
				if (date1 < date2) return 1
				return 0
			})
		}
	}

	const filtered = filterPostList(filter)

	// GET CURRENT USER FROM LOCAL STORAGE
	if (!currentUser) {
		return (
			<LoginRequire
				action={action}
				setAction={setAction}
			/>
		)
	} else {
		return (
			<div className="container ">
				<div className="row">
					<div className="col-lg-10 col-md-8 mx-auto my-auto">
						<div className="mt-4 mb-4">
							<div className="filter-and-search">
								<input style={{ width: '270px', height: '40px', borderRadius: '10px' }} type="search" placeholder=" Search by author..." onChange={(evt) => setAuthor(evt.target.value)} />
								<input style={{ width: '270px', height: '40px', borderRadius: '10px' }} type="search" placeholder=" Search by title..." onChange={(evt) => setSearchTerm(evt.target.value)} />
								<select style={{ width: '270px', height: '40px', borderRadius: '10px' }} id="filter" onChange={() => changeFilterType()}>
									<option value="0" defaultChecked='true'>Default order</option>
									<option value="1">Title (A - Z)</option>
									<option value="2">Title (Z - A)</option>
									<option value="3">Date (Oldest)</option>
									<option value="4">Date (Latest)</option>
								</select>
							</div>
						</div>

						<List
							itemLayout='vertical'
							size='small'
							pagination={{
								onChange: page => {
									// console.log(page);
									localStorage.setItem('currentPage', page);
								},
								pageSize: 5,
								defaultCurrent: currentPage,
								style: {
									textAlign: 'center',
									marginTop: '2em',
									marginBottom: '2em'
								}
							}}

							dataSource={filtered
								.filter(ele => ele.title.toLowerCase().includes(searchTerm.toLowerCase()))
								.filter(ele => ele.author.toLowerCase().includes(author.toLowerCase()))}
							renderItem={item => (
								<Post
									key={item.id}
									data={item}
									postList={postList}
									setPostList={setPostList}
								/>

							)}
						>

						</List>
					</div>

				</div>
			</div>
		)
	}
}

export default PostList
