let countSectionCertification = 0;

$("#btnAddSectionCertification").click(function ($e) {
  $e.preventDefault();
  countSectionCertification++;
  $("#sectionCertification").append(`
      <div id="sectionCertificationForm--${countSectionCertification}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionCertificationCollapse--${countSectionCertification}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseCertification--${countSectionCertification}" role="button" aria-expanded="false"
          aria-controls="collapseCertification--${countSectionCertification}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtCertificationName--${countSectionCertification}"></b>
            <span id="txtCertificationInstitute--${countSectionCertification}"></span>
            <span id="txtCertificationReleaseDate--${countSectionCertification}"></span>
            <span id="txtCertificationDate--${countSectionCertification}"></span>
            <b id="txtCertificationDefault--${countSectionCertification}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseCertification--${countSectionCertification}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-12">
              <input data-collapse="#collapseCertification--${countSectionCertification}" type="hidden" id="inputCertificationId--${countSectionCertification}">
              <label>Nama Sertifikasi <b class="text-danger">*</b> </label>
              <input autocomplete="off" id="inputCertificationName--${countSectionCertification}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtCertificationName--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtCertificationNameFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtCertificationNameFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtCertificationName--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtCertificationNameFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtCertificationNameFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isCertificationValueBlank(); autoSave(); validateRequire($(this))" >
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-12">
                <label>Lembaga <b class="text-danger">*</b> </label>
              <input data-collapse="#collapseCertification--${countSectionCertification}" autocomplete="off" id="inputCertificationInstitute--${countSectionCertification}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtCertificationInstitute--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtCertificationInstituteFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtCertificationInstituteFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtCertificationInstitute--${countSectionCertification}').text(' from ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtCertificationInstituteFrame--${countSectionCertification}').text(' from ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtCertificationInstituteFrame--${countSectionCertification}').text(' from ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isCertificationValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6">
              <label>Dikeluarkan pada <b class="text-danger">*</b> </label>
              <input data-collapse="#collapseCertification--${countSectionCertification}" autocomplete="off" id="inputCertificationReleaseDate--${countSectionCertification}" type="month" class="form-control required" oninput="
              if ($(this).val() === '') {
                  $('#txtCertificationReleaseDate--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                  $('#frame').contents().find('#txtCertificationReleaseDateFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                  $('#txtCertificationReleaseDateFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              } else {
                  $('#txtCertificationReleaseDate--${countSectionCertification}').text(' valid since ' + convertDate($(this).val()));
                  $('#frame').contents().find('#txtCertificationReleaseDateFrame--${countSectionCertification}').text(' valid since ' + convertDate($(this).val())).fadeTo(100, 0.3, function () { $(this).fadeTo(100, 1.0); });
                  $('#txtCertificationReleaseDateFrame--${countSectionCertification}').text(' valid since ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              }
              isCertificationValueBlank(); autoSave(); validateRequire($(this));" required="">
            </div>
            <div class="form-group col-md-6">
              <label>Berlaku Sampai <b class="text-danger">*</b> </label>
              <input data-collapse="#collapseCertification--${countSectionCertification}" autocomplete="off" id="inputCertificationValidityPeriod--${countSectionCertification}" class="form-control required" type="month" oninput="
                if($(this).val() === ''){
                    $('#txtCertificationDate--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtCertificationDateFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtCertificationDateFrame--${countSectionCertification}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                  $('#txtCertificationDate--${countSectionCertification}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtCertificationDateFrame--${countSectionCertification}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtCertificationDateFrame--${countSectionCertification}').text(' - ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isCertificationValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row">
              <div class="col-md-6"></div>
              <div class="col-md-6">
                  <div class="form-group form-check">
                      <input type="checkbox" class="form-check-input" id="certificationUnEnroll--${countSectionCertification}" onchange="disableEndDate($(this), '#inputCertificationValidityPeriod--${countSectionCertification}', 'certification')">
                      <label for="certificationUnEnroll--${countSectionCertification}" class="form-check-label">Berlaku seumur hidup.</label>
                  </div>
              </div>
          </div>
        </div>
      </div>
      <div id="sectionCertificationBtnDelete--${countSectionCertification}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionCertification(${countSectionCertification});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);
  $(`#sectionCertificationForm--${countSectionCertification}`)
    .hide()
    .fadeIn(500);

  // Frame di Live Preview sebelah kanan (sebelum diisi)
  $("#frame").contents().find("#txtCertificationFrame").append(`
        <tr id="txtCertificationFrame--${countSectionCertification}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtCertificationNameFrame--${countSectionCertification}"></b>
            <span id="txtCertificationInstituteFrame--${countSectionCertification}"></span>
            <span id="txtCertificationReleaseDateFrame--${countSectionCertification}"></span>
            <span id="txtCertificationDateFrame--${countSectionCertification}"></span>
            <b id="txtCertificationDefaultFrame--${countSectionCertification}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtCertificationFrame--${countSectionCertification}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  // Frame di dalam tombol 'Preview CV' (sebelum diisi)
  $("#txtCertificationFrame").append(`
        <tr id="txtCertificationFrame--${countSectionCertification}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtCertificationNameFrame--${countSectionCertification}"></b>
            <span id="txtCertificationInstituteFrame--${countSectionCertification}"></span>
            <span id="txtCertificationReleaseDateFrame--${countSectionCertification}"></span>
            <span id="txtCertificationDateFrame--${countSectionCertification}"></span>
            <b id="txtCertificationDefaultFrame--${countSectionCertification}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $(`#txtCertificationFrame--${countSectionCertification}`).fadeTo(
    100,
    0.3,
    function () {
      $(this).fadeTo(500, 1.0);
    }
  );

  for (let i = 0; i < countSectionCertification; i++) {
    let index = i + 1;

    if ($(`#collapseCertification--${index}`).hasClass("collapse show")) {
      $(`#sectionCertificationCollapse--${index}`).click();
    }
  }

  isCertificationValueBlank();
  renderAutocomplete();
});

function deletesectionCertification(id) {
  // countSectionCertification--;
  iziToast.question({
    timeout: 20000,
    close: false,
    overlay: true,
    displayMode: "once",
    id: "question",
    zindex: 999,

    message: "Yakin ingin menghapus data?",
    position: "center",
    buttons: [
      [
        "<button><b>Ya</b></button>",
        function (instance, toast) {
          $(`#sectionCertificationForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionCertificationBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtCertificationFrame--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide(
            {
              transitionOut: "fadeOut",
            },
            toast,
            "button"
          );

          deleteData(
            "/api/delete-user-certification/" +
              $(`#inputCertificationId--${id}`).val()
          )
            .then((r) => console.log(r))
            .catch((r) => console.log(r));
        },
        true,
      ],
      [
        "<button>Batal</button>",
        function (instance, toast) {
          instance.hide(
            {
              transitionOut: "fadeOut",
            },
            toast,
            "button"
          );
        },
      ],
    ],
  });
}

function isCertificationValueBlank() {
  for (var i = 0; i < countSectionCertification; i++) {
    var index = i + 1;
    if (
      $(`#inputCertificationName--${index}`).val() !== "" ||
      $(`#inputCertificationInstitute--${index}`).val() !== "" ||
      $(`#inputCertificationReleaseDate--${index}`).val() !== "" ||
      $(`#inputCertificationValidityPeriod--${index}`).val() !== ""
    ) {
      $(`#txtCertificationDefault--${index}`).hide();
      $("#frame")
        .contents()
        .find(`#txtCertificationDefaultFrame--${index}`)
        .hide();
      $(`#txtCertificationDefaultFrame--${index}`).hide();
    } else {
      $(`#txtCertificationDefault--${index}`).show();
      $("#frame")
        .contents()
        .find(`#txtCertificationDefaultFrame--${index}`)
        .show();
      $(`#txtCertificationDefaultFrame--${index}`).show();
    }
  }
}
