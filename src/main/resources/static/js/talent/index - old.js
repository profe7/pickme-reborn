function detail(nik) {
  $("#detailCard").LoadingOverlay("show");
  $.ajax({
    url: `/api/talent/${nik}`,
    method: "GET",
    success: (result) => {
      console.log(result);
      let root = $("#detailCard");
      var iframe = $("#talentDetail");
      iframe.attr(
        "src",
        `https://dev.cv-me.metrodataacademy.id/share/${result.userUrl}`
      );
      root.find(".invite-button").attr("href", `/talent/${nik}/create`);
      $("#detailCard").LoadingOverlay("hide", true);
    },
    error: (e) => {
      $("#detailCard").LoadingOverlay("hide", true);
      Swal.fire({
        position: "center",
        icon: "error",
        title: "Request failed to made.",
        showConfirmButton: false,
        timer: 1500,
      });
    },
  });
}
