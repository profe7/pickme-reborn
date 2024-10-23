let countSectionExperience = 0;

$("#btnAddSectionExperience").click(function ($e) {
  $e.preventDefault();
  countSectionExperience++;
  $("#sectionExperience").append(`
      <div id="sectionExperienceForm--${countSectionExperience}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionExperienceCollapse--${countSectionExperience}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseExperience--${countSectionExperience}" role="button" aria-expanded="false"
          aria-controls="collapseExperience--${countSectionExperience}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtExperienceName--${countSectionExperience}"></b>
            <span id="txtExperienceInstitute--${countSectionExperience}"></span>
            <span id="txtExperienceDate--${countSectionExperience}"></span>
            <b id="txtExperienceDefault--${countSectionExperience}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseExperience--${countSectionExperience}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-12">
              <input data-collapse="#collapseExperience--${countSectionExperience}" type="hidden" id="inputExperienceId--${countSectionExperience}">
              <label>Nama Pengalaman <b class="text-danger">*</b></label>
              <input autocomplete="off" id="inputExperienceName--${countSectionExperience}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtExperienceName--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtExperienceName--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isExperienceValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6">
              <label>Lembaga <b class="text-danger">*</b></label>
              <input data-collapse="#collapseExperience--${countSectionExperience}" autocomplete="off" id="inputExperienceInstitute--${countSectionExperience}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtExperienceInstitute--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceInstituteFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceInstituteFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtExperienceInstitute--${countSectionExperience}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceInstituteFrame--${countSectionExperience}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceInstituteFrame--${countSectionExperience}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isExperienceValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
            <div class="form-group col-md-6">
              <label>Bulan - Tahun <b class="text-danger">*</b></label>
              <input data-collapse="#collapseExperience--${countSectionExperience}" id="inputExperienceDate--${countSectionExperience}" class="form-control required" type="month" oninput="
                if($(this).val() === ''){
                    $('#txtExperienceDate--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceDateFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceDateFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtExperienceDate--${countSectionExperience}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtExperienceDateFrame--${countSectionExperience}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtExperienceDateFrame--${countSectionExperience}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isExperienceValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
           <div class="row">
            <div class="form-group col-md-12">
              <label>Jabatan <b class="text-danger">*</b></label>
              <input autocomplete="off" id="inputPositionName--${countSectionExperience}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtPositionName--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtPositionNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtPositionNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtPositionName--${countSectionExperience}').text(' as a ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtPositionNameFrame--${countSectionExperience}').text(' as a ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtPositionNameFrame--${countSectionExperience}').text(' as a ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isExperienceValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-12">
              <label>Deskripsi </label>
              <textarea style="height:100px" autocomplete="off" id="inputDescriptionName--${countSectionExperience}" type="text" class="form-control" oninput="
                if($(this).val() === ''){
                    $('#txtDescriptionName--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtDescriptionNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtDescriptionNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtDescriptionName--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtDescriptionNameFrame--${countSectionExperience}').text(', ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtDescriptionNameFrame--${countSectionExperience}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isExperienceValueBlank(); autoSave();"></textarea> 
            </div>
          </div>
        </div>
      </div>
      <div id="sectionExperienceBtnDelete--${countSectionExperience}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionExperience(${countSectionExperience});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);
  $(`#sectionExperienceForm--${countSectionExperience}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtExperienceFrame").append(`
        <tr id="txtListExperience--${countSectionExperience}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtExperienceNameFrame--${countSectionExperience}"></b>
            <span id="txtExperienceInstituteFrame--${countSectionExperience}"></span>
            <span id="txtPositionNameFrame--${countSectionExperience}"></span>
            <span id="txtDescriptionNameFrame--${countSectionExperience}"></span>
            <span id="txtExperienceDateFrame--${countSectionExperience}"></span>
            <b id="txtExperienceDefaultFrame--${countSectionExperience}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtListExperience--${countSectionExperience}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtExperienceFrame").append(`
        <tr id="txtListExperience--${countSectionExperience}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtExperienceNameFrame--${countSectionExperience}"></b>
            <span id="txtExperienceInstituteFrame--${countSectionExperience}"></span>
            <span id="txtExperienceDateFrame--${countSectionExperience}"></span>
            <b id="txtExperienceDefaultFrame--${countSectionExperience}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $(`#txtListExperience--${countSectionExperience}`).fadeTo(
    100,
    0.3,
    function () {
      $(this).fadeTo(500, 1.0);
    }
  );

  for (let i = 0; i < countSectionExperience; i++) {
    let index = i + 1;

    if ($(`#collapseExperience--${index}`).hasClass("collapse show")) {
      $(`#sectionExperienceCollapse--${index}`).click();
    }
  }

  isExperienceValueBlank();
  renderAutocomplete();
});

function deletesectionExperience(id) {
  // countSectionExperience--;
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
          $(`#sectionExperienceForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionExperienceBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListExperience--${id}`)
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
            "/api/delete-user-experience/" +
              $(`#inputExperienceId--${id}`).val()
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

function isExperienceValueBlank() {
  for (var i = 0; i < countSectionExperience; i++) {
    var index = i + 1;
    if (
      $(`#inputExperienceName--${index}`).val() !== "" ||
      $(`#inputExperienceInstitute--${index}`).val() !== "" ||
      $(`#inputExperienceDate--${index}`).val() !== ""
    ) {
      $(`#txtExperienceDefault--${index}`).hide();
      $("#frame")
        .contents()
        .find(`#txtExperienceDefaultFrame--${index}`)
        .hide();
      $(`#txtExperienceDefaultFrame--${index}`).hide();
    } else {
      $(`#txtExperienceDefault--${index}`).show();
      $("#frame")
        .contents()
        .find(`#txtExperienceDefaultFrame--${index}`)
        .show();
      $(`#txtExperienceDefaultFrame--${index}`).show();
    }
  }
}
