import React, { useState, useRef } from 'react';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { Editor } from '@tinymce/tinymce-react';
import axios from 'axios';
import { useHistory } from 'react-router-dom';

const CreatePost = ({ postList, setPostList }) => {
	const currentUser = JSON.parse(localStorage.getItem('currentUser'));

	const editorRef = useRef(null);

	// const history = useHistory();

	const formik = useFormik({
		initialValues: {
			title: '',
			description: '',
		},
		validationSchema: Yup.object({
			title: Yup.string()
				.required('Title should not be empty'),
			description: Yup.string()
				.required('Description should not be empty'),
		}),
		onSubmit: async (values) => {
			// alert(JSON.stringify(values, null, 2))
			if (editorRef.current.getContent() === '') {
				// console.log('editor content: ', editorRef.current.getContent());
				setIsContentEmpty(true);
				return;
			}

			const newPost = {
				authorId: currentUser.id,
				title: values.title,
				description: values.description,
				content: editorRef.current.getContent(),
				createdDate: new Date()
			}

			try {
				const createPostResult = await axios.post(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/create?authorId=${currentUser.id}`, newPost, {
					headers: {
						Authorization: `Bearer ${currentUser.token}`
					}
				});

				// console.log('new post: ', newPost);
				// console.log('post list: ', postList);

				// const newPostList = [...postList, createPostResult.data];

				// setPostList(newPostList);

				// setTimeout(() => {
				// 	history.push('/content');
				// }, 1500);

				// console.log('create post result: ', createPostResult);

			} catch (err) {
				console.log(err);
			}

			try {
				const getAllPost = await axios.get(`http://localhost:8080/api/v1/posts/`, {
					headers: {
						Authorization: `Bearer ${currentUser.token}`
					}
				});
				setPostList(getAllPost.data);
				localStorage.setItem('postList', JSON.stringify(getAllPost.data));
			} catch (err) {
				console.log(err);
			}

		}
	});

	const [isContentEmpty, setIsContentEmpty] = useState(false);


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
			console.log('result: ', res.data);

			console.log('editor: ', editorRef.current.getContent()); 

			// const newContent = editorRef.current.getContent() + `<div><img src=${res.data.secure_url} alt="" width='400px'></div>`

			// editorRef.current.setContent(newContent);

			success(res.data.secure_url);



		}).catch(err => {

			failure('err: ', err);
		});
	};

	return (
		<form onSubmit={formik.handleSubmit}>

			<table style={{
				marginLeft: 'auto',
				marginRight: 'auto'
			}}>
				<thead>

				</thead>
				<tbody>
					<tr>
						{/* <td>
							<label htmlFor="title"><i>Title</i></label>
						</td> */}
						<td>
							<input
								placeholder="A compact and concise title is always recommended. "
								id="title"
								name="title"
								type="text"
								onChange={formik.handleChange}
								onBlur={formik.handleBlur}
								value={formik.values.title}
								style={{
									width: '1000px',
									height: '50px',
									border: '1px solid #CCCCCC',
									borderRadius: '10px'
								}}
							/>
						</td>
					</tr>
					<tr>
						{/* <td></td> */}
						<td>
							{formik.touched.title && formik.errors.title
								? (<div style={{ marginTop: '4px', marginBottom: '4px', color: 'red' }}>{formik.errors.title}</div>)
								: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}
						</td>
					</tr>
					<tr>
						{/* <td>
							<label htmlFor="description"><i>Description</i></label>
						</td> */}
						<td>
							<input
								placeholder="A brief description of your post."
								id="description"
								name="description"
								type="text"
								onChange={formik.handleChange}
								onBlur={formik.handleBlur}
								value={formik.values.description}
								style={{
									width: '1000px',
									height: '50px',
									border: '1px solid #CCCCCC',
									borderRadius: '10px'
								}}
							/>
						</td>
					</tr>
					<tr>
						{/* <td></td> */}
						<td>
							{formik.touched.description && formik.errors.description
								? (<div style={{ marginTop: '4px', marginBottom: '4px', color: 'red' }}>{formik.errors.description}</div>)
								: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}
						</td>
					</tr>
					<tr>
						{/* <td>
							<label><i>Content</i></label>
						</td> */}
						<td>
							<div

							>

								<Editor
									// id='content'
									// name='content'
									onInit={(evt, editor) => editorRef.current = editor}
									onChange={formik.handleChange}
									// initialValue={formik.values.content}
									init={{
										placeholder: 'Create your content here...',

										height: '60vh',

										width: '1000px',

										forced_root_block : 'div',
										menubar: false,
										plugins: [
											'advlist autolink lists link image charmap print preview anchor',
											'searchreplace visualblocks code fullscreen',
											'insertdatetime media table paste code help wordcount image media'
										],
										toolbar: 'undo redo | formatselect | ' +
											'bold italic backcolor | alignleft aligncenter ' +
											'alignright alignjustify | bullist numlist outdent indent | ' +
											'removeformat | wordcount | visualblocks | image | media | help',
										toolbar_mode: 'sliding',
										content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',

										images_upload_handler: example_image_upload_handler
									}}
								/>
							</div>

						</td>
					</tr>
					<tr>
						{/* <td></td> */}
						<td>
							{isContentEmpty && editorRef.current.getContent() === ''
								? (<div style={{ marginTop: '4px', marginBottom: '4px', color: 'red' }}>Content should not be empty</div>)
								: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}
						</td>
					</tr>
					<tr>
						{/* <td></td> */}
						<td>
							<button
								className='btn btn-outline-primary shadow-none'
								style={{
									width: '100px'
								}}
								type='submit'
							>
								Submit
							</button>
							<button
								className='btn btn-outline-danger ml-3 shadow-none'
								type='reset'
								style={{
									width: '100px'
								}}

							>
								Reset
							</button>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	)
}


export default CreatePost;
