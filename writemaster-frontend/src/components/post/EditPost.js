import React, { Fragment, useRef } from 'react'
import { useParams, useHistory } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import axios from 'axios'
import Helper from '../../utils/Helper'
import { Editor } from '@tinymce/tinymce-react';

const EditPost = ({
	postList,
	setPostList
}) => {

	const example_image_upload_handler = (blobInfo, success, failure, progress) => {
		const cloudinaryName = "writemaster-platform";

		const folderName = 'images_used_in_posts';

		const fileType = "image";
		const url = `https://api.cloudinary.com/v1_1/${cloudinaryName}/${fileType}/upload`;

		const formData = new FormData();
		formData.append("upload_preset", cloudinaryName);
		formData.append("folder", folderName);
		formData.append('file', blobInfo.blob(), blobInfo.filename());

		axios.post(url, formData, {
			header: {
				"Content-Type": "application/x-www-form-urlencoded",
			}
		}).then(res => {
			// console.log('result: ', res.data);

			// console.log('editor: ', editorRef.current.getContent()); 

			success(res.data.secure_url);

		}).catch(err => {

			failure('err: ', err);
		});
	};

	const history = useHistory()
	const id = parseInt(useParams().id)

	const editorRef = useRef(null);

	// const post = postList.find(ele => ele.id === id);

	let post

	if (postList.length !== 0) {
		// console.log(postList);
		post = postList.find(ele => ele.id === id)

		// console.log('post from curren postList');
	} else {
		// let postList = ;
		post = JSON.parse(localStorage.getItem('postList')).find(ele => ele.id === id)
		// console.log("post from localStorage");
	}

	const defaultDateTime = Helper.getTodayDateTime(post.createdDate)

	const currentUser = JSON.parse(localStorage.getItem('currentUser')) || null

	// GET DATA FROM LOCAL STORAGE
	if (post.authorId !== currentUser.id) {
		return (
			<h2>You are not author of this post.</h2>
		)
	} else {
		return (
			<Formik
				initialValues={{ title: post.title, description: post.description, content: post.content, createdDate: defaultDateTime }}
				validate={values => {
					const errors = {

					}
					if (!values.title) {
						errors.title = 'Title should not be empty'
					}
					if (!values.description) {
						errors.description = 'Description should not be empty'
					}
					if (!values.content) {
						errors.content = 'Content should not be empty'
					}

					return errors
				}}

				onSubmit={(values, { setSubmitting }) => {
					const payload = {
						author: currentUser.username,
						authorId: currentUser.id,
						content: editorRef.current.getContent(),
						title: values.title,
						description: values.description,
						createdDate: values.createdDate,
						id: id
					}

					console.log('Update post payload: ', payload);

					axios
						.put(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/update/${id}`, payload, {
						headers: {
							Authorization: `Bearer ${currentUser.token}`
						}
					}).then(response => {
						console.log('Response: ', response)
						if (response.status === 200) {
							setPostList(postList => postList.map(ele => {
								if (ele.id === id) {
									ele.title = response.data.title
									ele.description = response.data.description
									ele.content = response.data.content
									ele.createdDate = response.data.createdDate
									ele.info = `Posted by ${post.author} on ${Helper.formatCreatedDate(response.data.createdDate)} - ${Helper.timeToReadCalculator(post)} ${Helper.timeToReadCalculator(post) < 1 ? 'seconds' : 'minutes'} read`
								}
								return ele
							}))
							// history.push(`/content`);
							history.push(`/postdetail/${post.id}`);
						}
					}).catch(err => {
						console.log(err);
						history.push(`/postedit/${id}`)
					})
				}}
			>
				{({ isSubmitting }) => (
					<div className='container'>
						<div className="form-group mt-3 mb-3">


							{/* <Fragment> */}
							<h3>Edit post: {post.title}</h3>
							<Form>
								<div className='form-group'>
									<label htmlFor="title">Title</label><br />
									<Field
										className="form-control"
										id="title"
										component="textarea"
										rows='2'
										type="text"
										name="title"
										placeholder='...'
									/>

									<ErrorMessage
										className='mt-2 mb-2'
										name="title"
										component="div"
										style={{ color: 'red' }}
									/>

								</div>

								<div className="form-group mb-3">
									<label htmlFor="description">Description</label><br />
									<Field
										className="form-control"
										id="description"
										component="textarea"
										rows='2'
										type="text"
										name="description"
										placeholder='...'
									/>
									<ErrorMessage
										name="description"
										component="div"
										style={{ color: 'red' }}
									/>
								</div>

								<div className="form-group mb-3">
									<label htmlFor="content">Content</label><br />
									

									<>
										<Editor
											onInit={(evt, editor) => editorRef.current = editor}
											initialValue={post.content}
											init={{
												height: 600,
												menubar: false,
												plugins: [
													'advlist autolink lists link image charmap print preview anchor',
													'searchreplace visualblocks code fullscreen',
													'insertdatetime media table paste code help wordcount image'
												],
												toolbar: 'undo redo | formatselect | ' +
													'bold italic backcolor | alignleft aligncenter ' +
													'alignright alignjustify | bullist numlist outdent indent | ' +
													'removeformat | image | wordcount |help',
												toolbar_mode: 'sliding',
												content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
												images_upload_handler: example_image_upload_handler

											}
											}
										/>
									</>
								</div>

								<div className="form-group mb-4">
									<button
										className="btn btn-warning btn-small form-control shadow-none"
										type="submit"
										disabled={isSubmitting}
									>
										UPDATE
									</button>
								</div>

							</Form>
						</div>
					</div>
				)
				}
			</Formik >

		)
	}
}

export default EditPost;
