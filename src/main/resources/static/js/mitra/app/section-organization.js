let countSectionOrganization = 0;

$("#btnAddSectionOrganization").click(function ($e) {
  $e.preventDefault();
  countSectionOrganization++;
  $("#sectionOrganization").append(`
      <div id="sectionOrganizationForm--${countSectionOrganization}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionOrganizationCollapse--${countSectionOrganization}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseOrganization--${countSectionOrganization}" role="button" aria-expanded="false"
          aria-controls="collapseOrganization--${countSectionOrganization}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtOrganizationPosition--${countSectionOrganization}"></b>
            <b id="txtOrganizationName--${countSectionOrganization}"></b>
            <span id="txtOrganizationStartDate--${countSectionOrganization}"></span>
            <span id="txtOrganizationEndDate--${countSectionOrganization}"></span>
            <b id="txtOrganizationDefault--${countSectionOrganization}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseOrganization--${countSectionOrganization}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-6">
              <input type="hidden" id="inputOrganizationId--${countSectionOrganization}">
              <label>Nama Organisasi </label>
              <input data-collapse="#collapseOrganization--${countSectionOrganization}" autocomplete="autocomplete_off_cv_org_name" id="inputOrganizationName--${countSectionOrganization}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtOrganizationName--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationNameFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationNameFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtOrganizationName--${countSectionOrganization}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationNameFrame--${countSectionOrganization}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationNameFrame--${countSectionOrganization}').text(' at '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isOrganizationValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
           <div class="form-group col-md-6">
  <label>Posisi</label>
  <select data-collapse="#collapseOrganization--${countSectionOrganization}" id="inputOrganizationPosition--${countSectionOrganization}" class="form-control required" onchange="
    $('#txtOrganizationPosition--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
    $('#frame').contents().find('#txtOrganizationPositionFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
    $('#txtOrganizationPositionFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
    isOrganizationValueBlank(); autoSave(); validateRequire($(this))" required="">
    <option value="">Pilih Posisi</option>
    <option value="KETUA">Ketua</option>
    <option value="WAKIL_KETUA">Wakil Ketua</option>
    <option value="ANGGOTA">Anggota</option>
  </select>
</div>

          </div>
          <div class="row">
            <div class="form-group form-check ml-3">
                <input type="checkbox" id="organizationUnEnroll--${countSectionOrganization}" class="form-check-input" onchange="disableEndDate($(this), '#inputOrganizationEndDate--${countSectionOrganization}', 'organization')">
                <label for="organizationUnEnroll--${countSectionOrganization}" class="form-check-label">Saya masih aktif diorganisasi ini</label>
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6">
            <label>Dimulai dari </label>
              <input data-collapse="#collapseOrganization--${countSectionOrganization}" autocomplete="off" id="inputOrganizationStartDate--${countSectionOrganization}" type="month" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtOrganizationStartDate--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationStartDateFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationStartDateFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtOrganizationStartDate--${countSectionOrganization}').text(' for the period of ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationStartDateFrame--${countSectionOrganization}').text(' for the period of ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationStartDateFrame--${countSectionOrganization}').text(' for the period of ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                $('#inputOrganizationEndDate--${countSectionOrganization}').attr('min', $(this).val())
                isOrganizationValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
            <div class="form-group col-md-6">
            <label>Berakhir pada</label>
              <input data-collapse="#collapseOrganization--${countSectionOrganization}" id="inputOrganizationEndDate--${countSectionOrganization}" class="form-control required" type="month" oninput="
                if($(this).val() === ''){
                    $('#txtOrganizationEndDate--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationEndDateFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationEndDateFrame--${countSectionOrganization}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtOrganizationEndDate--${countSectionOrganization}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtOrganizationEndDateFrame--${countSectionOrganization}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtOrganizationEndDateFrame--${countSectionOrganization}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isOrganizationValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
        </div>
      </div>
      <div id="sectionOrganizationBtnDelete--${countSectionOrganization}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionOrganization(${countSectionOrganization});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);
  $(`#sectionOrganizationForm--${countSectionOrganization}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtOrganizationFrame").append(`
        <tr id="txtListOrganization--${countSectionOrganization}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtOrganizationPositionFrame--${countSectionOrganization}"></b>
            <b id="txtOrganizationNameFrame--${countSectionOrganization}"></b>
            <span id="txtOrganizationStartDateFrame--${countSectionOrganization}"></span>
            <span id="txtOrganizationEndDateFrame--${countSectionOrganization}"></span>
            <b id="txtOrganizationDefaultFrame--${countSectionOrganization}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtListOrganization--${countSectionOrganization}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtOrganizationFrame").append(`
        <tr id="txtListOrganization--${countSectionOrganization}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtOrganizationPositionFrame--${countSectionOrganization}"></b>
            <b id="txtOrganizationNameFrame--${countSectionOrganization}"></b>
            <span id="txtOrganizationStartDateFrame--${countSectionOrganization}"></span>
            <span id="txtOrganizationEndDateFrame--${countSectionOrganization}"></span>
            <b id="txtOrganizationDefaultFrame--${countSectionOrganization}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $(`#txtListOrganization--${countSectionOrganization}`).fadeTo(
    100,
    0.3,
    function () {
      $(this).fadeTo(500, 1.0);
    }
  );

  for (let i = 0; i < countSectionOrganization; i++) {
    let index = i + 1;

    if ($(`#collapseOrganization--${index}`).hasClass("collapse show")) {
      $(`#sectionOrganizationCollapse--${index}`).click();
    }
  }

  isOrganizationValueBlank();

  renderAutocomplete();
});

function deletesectionOrganization(id) {
  // countSectionOrganization--;
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
          $(`#sectionOrganizationForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionOrganizationBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListOrganization--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");

          deleteData(
            "/api/delete-user-organization/" +
              $(`#inputOrganizationId--${id}`).val()
          )
            .then((r) => console.log(r))
            .catch((r) => console.log(r));
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

function isOrganizationValueBlank() {
  for (var i = 0; i < countSectionOrganization; i++) {
    var index = i + 1;
    if (
      $(`#inputOrganizationName--${index}`).val() !== "" ||
      $(`#inputOrganizationStartDate--${index}`).val() !== "" ||
      $(`#inputOrganizationEndDate--${index}`).val() !== ""
    ) {
      $(`#txtOrganizationDefault--${index}`).hide();
      $("#frame")
        .contents()
        .find(`#txtOrganizationDefaultFrame--${index}`)
        .hide();
      $(`#txtOrganizationDefaultFrame--${index}`).hide();
    } else {
      $(`#txtOrganizationDefault--${index}`).show();
      $("#frame")
        .contents()
        .find(`#txtOrganizationDefaultFrame--${index}`)
        .show();
      $(`#txtOrganizationDefaultFrame--${index}`).show();
    }
  }
}
