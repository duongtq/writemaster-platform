import React, { useState } from "react";
import default_avatar from "../assets/default_avatar.png";
import axios from "axios";
import $ from "jquery";

const UploadAvatarForm = () => {
  const currentUser = JSON.parse(localStorage.getItem("currentUser"));

  console.log("currentUser", currentUser);

  console.log("current user avatar url: ", currentUser.avatarUrl);

  const [selectedImage, setSelectedImage] = useState(currentUser.avatarUrl || null);

  const cloudinaryName = "writemaster-platform";
  const fileType = "image";
  const url = `https://api.cloudinary.com/v1_1/${cloudinaryName}/${fileType}/upload`;

  const submitImage = async () => {
    $("#avatarUpload").modal({
      show: true,
    });

    if (currentUser.avatarUrl) {
      return;
    }

    const files = document.querySelector("[type=file]").files;

    const formData = new FormData();

    const image = files[0];

    formData.append("file", image);

    // console.log("Image ?", image);
    formData.append("upload_preset", cloudinaryName);
    formData.append("file", image);
    formData.append("folder", "writemaster-avatars");

    try {
      const uploadImageResult = await axios.post(url, formData, {
        header: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      });

      console.log('XYZ: ', uploadImageResult.data);

      localStorage.setItem('currentAvatarInfo', JSON.stringify(uploadImageResult));

      console.log(
        "Image URL in Cloudinary: ",
        uploadImageResult.data.secure_url
      );

      const updateImagePayload = {
        authorId: currentUser.id,
        avatarUrl: uploadImageResult.data.secure_url
      }

      const saveImageToDBResult = await axios.post(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/authors/avatar`, updateImagePayload, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`,
        },
      });

      console.log('update avatar result: ', saveImageToDBResult.data);

      currentUser.avatarUrl = saveImageToDBResult.data.avatarUrl;

      localStorage.setItem('currentUser', JSON.stringify(currentUser));

      localStorage.setItem('currentPublicId', getAvatarPublicId(currentUser.avatarUrl));

      
    } catch (error) {
      console.log(error);
    }
  };

  const getAvatarPublicId = (avatarUrl) => {
    const xxx = avatarUrl.split('/');
    const yyy = xxx[xxx.length - 1].split('.');

    const public_id = `${xxx[xxx.length - 2]}/${yyy[0]}`;
    return public_id;
  }

  const removeImage = async () => {
    

    // console.log('Remove Image work');

    const updateImagePayload = {
      authorId: currentUser.id,
      avatarUrl: localStorage.getItem('currentPublicId')
    }

    // console.log('remove payload: ', updateImagePayload);

    const removeImageFromDBResult = await axios.delete(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/authors/avatar`, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`,
      },
      data: updateImagePayload
    });

    // console.log('delete avatar result: ', removeImageFromDBResult.data);

    // const currentAvatarInfo = JSON.parse(localStorage.getItem('currentAvatarInfo'));

    // console.log('current avatar info: ', currentAvatarInfo);

    currentUser.avatarUrl = removeImageFromDBResult.data.avatarUrl;

    localStorage.setItem('currentUser', JSON.stringify(currentUser));

    

    setSelectedImage(null);

    $("#avatarRemove").modal({
      show: true,
    });
  }

  return (
    <div style={{ display: "flex", justifyContent: "flex-start" }}>
      <div
        className="modal fade"
        id="avatarUpload"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className='modal-dialog' role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLabel">
                Upload avatar
              </h5>
              <button
                type="button"
                className="close"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">Image uploaded successfully.</div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-outline-primary shadow-none"
                data-dismiss='modal'
                style={{ width: '100px', height: '40px' }}
              >
                OK
              </button>
            </div>
          </div>
        </div>
      </div>

      <div
        className="modal fade"
        id="avatarRemove"
        tabIndex="-1"
        role="dialog"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
      >
        <div className='modal-dialog' role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLabel">
                Remove avatar
              </h5>
              <button
                type="button"
                className="close"
                data-dismiss="modal"
                aria-label="Close"
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">Avatar removed successfully.</div>
            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-outline-primary shadow-none"
                data-dismiss='modal'
                style={{ width: '100px', height: '40px' }}
              >
                OK
              </button>
            </div>
          </div>
        </div>
      </div>

      <img
        style={{ border: "1px dashed red", borderRadius: "10%" }}
        width={"150px"}
        height={"150px"}
        src={
          selectedImage ? (selectedImage instanceof File ? URL.createObjectURL(selectedImage) : selectedImage) : default_avatar
        }
        alt=""
      />
      <div
        className="mt-2 ml-4"
        style={{
          display: "flex",
          justifyContent: "space-between",
          flexDirection: "column",
        }}
      >
        <label className="btn btn-outline-primary" htmlFor="avatar" style={{ cursor: 'pointer' }}>
          <input
            id='avatar'
            type='file'
            name="avatar"
            title=" "
            disabled={selectedImage !== null}
            style={{ display: 'none' }}
            accept="image/png, image/gif, image/jpeg"

            onChange={(event) => {
              if (event.target.files[0].size > 1000000) {
                // TODO: Modal to inform image too big

                alert('Maximum image size is 1MB');
                event.target.value = "";
                return;
              };
              console.log(event.target.files[0]);
              setSelectedImage(event.target.files[0]);
            }}
          />Choose Image
        </label>

        <label className="btn btn-outline-danger" style={{ width: "150px" }}>
          <input
            className='btn btn-danger shadow-none'
            style={{ display: "none" }}
            disabled={selectedImage === null}
            onClick={() => removeImage()}
          />
          Remove
        </label>

        <label className="btn btn-outline-info" style={{ width: "150px" }}>
          <input
            style={{ display: "none" }}
            disabled={selectedImage === null}
            onClick={() => submitImage()}
          />
          Save
        </label>

      </div>
    </div >
  );
};

export default UploadAvatarForm;
