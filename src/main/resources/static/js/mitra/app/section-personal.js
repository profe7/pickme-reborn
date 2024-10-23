// $("#inputProfilePicture").change(function () {
//   var fileName = $(this).val();
//   var file = $(this).get(0).files[0];
//   if (file) {
//     if (file.size < 1000000) {
//       var reader = new FileReader();
//       reader.onload = function () {
//         $("#profilePictureModalPreview").html("").croppie("destroy");
//         $uploadCrop = $("#profilePictureModalPreview").croppie({
//           url: reader.result,
//           enforceBoundary: true,
//           enableExif: false,
//           viewport: {
//             width: 220,
//             height: 300,
//             type: "square",
//           },
//           boundary: {
//             width: 450,
//             height: 450,
//           },
//         });
//       };
//       reader.readAsDataURL(file);
//       $("#profilePictureModal").modal("show");
//     } else {
//       iziToast.error({
//         title: "Gagal",
//         message: `File melebihi batas maksimal upload, <b>batas maksimal 1MB</b>`,
//         position: "topCenter",
//       });
//     }
//   }
// });

// function saveCrop() {
//   $("#profilePicturePreview")
//     .removeClass("is-invalid border border-danger")
//     .hide();
//   $("#frame").contents().find("#imgProfileImagePreview").hide();
//   $("#imgProfileImagePreview").hide();
//   $("#invalidPhoto").hide();
//   $(".loadingImage").show();

//   $uploadCrop
//     .croppie("result", {
//       type: "blob",
//       size: "viewport",
//     })
//     .then(function (resp) {
//       let form = new FormData();
//       form.append("file", resp);

//       let settings = {
//         url: "/api/aws/photo/upload",
//         method: "POST",
//         timeout: 0,
//         processData: false,
//         mimeType: "multipart/form-data",
//         contentType: false,
//         data: form,
//       };

//       $.ajax(settings)
//         .done(function (response) {
//           var jx = JSON.parse(response);
//           // Use the updated toDataURL function
//           // toDataURL(jx.data.profilePicture, (dataUrl) => {
//           $("#profilePicturePreview")
//             .attr("src", jx.data)
//             .fadeTo(100, 0.3, function () {
//               $(this).fadeTo(500, 1.0);
//             });

//           $("#frame")
//             .contents()
//             .find("#imgProfileImagePreview")
//             .attr("src", jx.data)
//             .fadeTo(100, 0.3, function () {
//               $(this).fadeTo(500, 1.0);
//             });

//           $("#frame").contents().find("#imgProfileImagePreview").show();
//           $("#imgProfileImagePreview").show();
//           $("#profilePicturePreview").show().trigger("change");
//           $(".loadingImage").hide();
//           // });

//           autoSave();
//         })
//         .fail((err) => {
//           console.log(err);
//         });
//     });
// }

function deletePhoto() {
  iziToast.error({
    timeout: 20000,
    close: false,
    overlay: true,
    displayMode: "once",
    id: "question",
    zindex: 999,
    message: "Yakin ingin menghapus photo?",
    position: "topCenter",
    buttons: [
      [
        '<button><b><i class="fa fa-trash"></i>Ya</b></button>',
        function (instance, toast) {
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
          $("#profilePicturePreview")
            .attr(
              "src",
              $("#inputSrcProfilePicturePreview").val() + "no_image.jpg"
            )
            .fadeTo(100, 0.3, function () {
              $(this).fadeTo(500, 1.0).trigger("change");
            });
          $("#frame")
            .contents()
            .find("#imgProfileImagePreview")
            .attr(
              "src",
              $("#inputSrcProfilePicturePreview").val() + "no_image.jpg"
            )
            .fadeTo(100, 0.3, function () {
              $(this).fadeTo(500, 1.0);
            });
          $("#imgProfileImagePreview")
            .attr(
              "src",
              $("#inputSrcProfilePicturePreview").val() + "no_image.jpg"
            )
            .fadeTo(100, 0.3, function () {
              $(this).fadeTo(500, 1.0);
            });

          document.cookie = `profilePicture=${
            $("#inputSrcProfilePicturePreview").val() + "user.png"
          }; ;path=/`;
          autoSave();
        },
        true,
      ],
      [
        "<button>Batal</button>",
        function (instance, toast) {
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");
        },
      ],
    ],
  });
}
