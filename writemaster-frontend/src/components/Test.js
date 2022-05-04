import React from 'react'
import { useFormik } from 'formik'
import * as Yup from 'yup'

const SignupForm = () => {
	const formik = useFormik({
	  initialValues: {
	    title: '',
	    description: '',
	    content: ''
	  },
	  validationSchema: Yup.object({
	    title: Yup.string()
	      .required('Required'),
	    description: Yup.string()
	      .required('Required'),
	    content: Yup.string()
	      .required('Required')
	  }),
	  onSubmit: values => {
	    alert(JSON.stringify(values, null, 2))
	  }
	});

	return (
		<form onSubmit={formik.handleSubmit}>
			<label htmlFor="title">Title</label>
			<input
				placeholder="Title here..."
				id="title"
				name="title"
				type="text"
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.title}
			/>

			{formik.touched.title && formik.errors.title
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.title}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}

			<label htmlFor="description">Description</label>
			<input
				placeholder="Description"
				id="description"
				name="description"
				type="text"
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.description}
			/>
			{formik.touched.description && formik.errors.description
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.description}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}

			<label htmlFor="content">Content</label>
			<input
				placeholder="content"
				id="content"
				name="content"
				type='text'
				onChange={formik.handleChange}
				onBlur={formik.handleBlur}
				value={formik.values.content}
			/>
			{formik.touched.content && formik.errors.content
				? (<div style={{ marginTop: '4px', marginBottom: '4px' }}>{formik.errors.content}</div>)
				: (<div style={{ marginTop: '4px', marginBottom: '4px' }}>&nbsp;</div>)}

			<button type="submit">Submit</button>
	  </form>
	)
}

export default SignupForm
