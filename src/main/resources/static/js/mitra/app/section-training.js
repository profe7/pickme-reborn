let countSectionTraining = 0;

$("#btnAddSectionTraining").click(function ($e) {
  $e.preventDefault();
  countSectionTraining++;
  $("#sectionTraining").append(`
    <div id="sectionTrainingForm--${countSectionTraining}" class="border rounded py-3 col-sm-11 mt-3 section-list">
      <div id="sectionTrainingCollapse--${countSectionTraining}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
        data-toggle="collapse" href="#collapseTraining--${countSectionTraining}" role="button" aria-expanded="false"
        aria-controls="collapseTraining--${countSectionTraining}">
        <div class="col-md-11 col-xs-11">
          <span class="bullet"></span>
          <b id="txtTrainingName--${countSectionTraining}"></b>
          <span id="txtTrainingDate--${countSectionTraining}"></span>
          <b id="txtTrainingDefault--${countSectionTraining}">(Anda belum mengisi)</b>
        </div>
        <div class="col-md-1 col-xs-1">
          <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
        </div>
      </div>
      <div class="collapse container" id="collapseTraining--${countSectionTraining}">
        <div class="mt-4"></div>
        <div class="row">
          <div class="form-group col-md-6">
            <input type="hidden" id="inputTrainingId--${countSectionTraining}">
            <label>Nama Pelatihan </label>
            <input data-collapse="#collapseTraining--${countSectionTraining}" autocomplete="off" id="inputTrainingName--${countSectionTraining}" type="text" class="form-control required" oninput="
              if($(this).val() === ''){
                  $('#txtTrainingName--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingNameFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingNameFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              } else {      
                  $('#txtTrainingName--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingNameFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingNameFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              }
              isTrainingValueBlank(); autoSave(); validateRequire($(this))" required="">
          </div>
          <div class="form-group col-md-6">
          <label>Bulan - Tahun </label>
            <input data-collapse="#collapseTraining--${countSectionTraining}" autocomplete="off" id="inputTrainingDate--${countSectionTraining}" type="month" class="form-control required" oninput="
              if($(this).val() === ''){
                  $('#txtTrainingDate--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingDateFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingDateFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              } else {
                  $('#txtTrainingDate--${countSectionTraining}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingDateFrame--${countSectionTraining}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingDateFrame--${countSectionTraining}').text('(' + convertDate($(this).val()) + ')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              }
              isTrainingValueBlank(); autoSave(); validateRequire($(this))" required="">
          </div>
        </div>
        <div class="row>
        <div class="form-group col-md-6">
            <label>Syllabus </label>
            <textarea textarea data-collapse="#collapseTraining--${countSectionTraining}" autocomplete = "off" id="inputTrainingSyllabus--${countSectionTraining}" class="form-control" style="height: 50px;" oninput="
              if($(this).val() === ''){
                  $('#txtTrainingSyllabus--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingSyllabusFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingSyllabusFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              } else {      
                  $('#txtTrainingSyllabus--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtTrainingSyllabusFrame--${countSectionTraining}').text(', '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtTrainingSyllabusFrame--${countSectionTraining}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              }
              isTrainingValueBlank(); autoSave();"></textarea>
          </div>
        </div>
      </div>
    </div>
    <div id="sectionTrainingBtnDelete--${countSectionTraining}" class="row col-sm-1 mt-4">
      <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
        onclick="deletesectionTraining(${countSectionTraining});">
        <i class="text-bottom fa fa-trash"></i>
      </a>
    </div>
  `);
  $(`#sectionTrainingForm--${countSectionTraining}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtTrainingFrame").append(`
      <tr id="txtListTraining--${countSectionTraining}">
        <td>
          <span class="bullet"></span>
        </td>
        <td>
          <b id="txtTrainingNameFrame--${countSectionTraining}"></b>
          <span id="txtTrainingSyllabusFrame--${countSectionTraining}"></span>
          <span id="txtTrainingDateFrame--${countSectionTraining}"></span>
          <b id="txtTrainingDefaultFrame--${countSectionTraining}">_________________________________________________</b>
          <br>
        </td>
      </tr>`);
  $("#frame")
    .contents()
    .find(`#txtListTraining--${countSectionTraining}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtTrainingFrame").append(`
      <tr id="txtListTraining--${countSectionTraining}">
        <td>
          <span class="bullet"></span>
        </td>
        <td>
          <b id="txtTrainingNameFrame--${countSectionTraining}"></b>
          <span id="txtTrainingSyllabusFrame--${countSectionTraining}"></span>
          <span id="txtTrainingDateFrame--${countSectionTraining}"></span>
          <b id="txtTrainingDefaultFrame--${countSectionTraining}">_________________________________________________</b>
          <br>
        </td>
      </tr>`);
  $(`#txtListTraining--${countSectionTraining}`).fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

  for (let i = 0; i < countSectionTraining; i++) {
    let index = i + 1;

    if ($(`#collapseTraining--${index}`).hasClass("collapse show")) {
      $(`#sectionTrainingCollapse--${index}`).click();
    }
  }

  isTrainingValueBlank();
  renderAutocomplete();
});

function deletesectionTraining(id) {
  // countSectionTraining--;
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
          $(`#sectionTrainingForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionTrainingBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListTraining--${id}`)
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
            "/api/delete-user-certificate/" + $(`#inputTrainingId--${id}`).val()
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

function isTrainingValueBlank() {
  for (var i = 0; i < countSectionTraining; i++) {
    var index = i + 1;
    if (
      $(`#inputTrainingName--${index}`).val() !== "" ||
      $(`#inputTrainingDate--${index}`).val() !== ""
    ) {
      $(`#txtTrainingDefault--${index}`).hide();
      $("#frame").contents().find(`#txtTrainingDefaultFrame--${index}`).hide();
      $(`#txtTrainingDefaultFrame--${index}`).hide();
    } else {
      $(`#txtTrainingDefault--${index}`).show();
      $("#frame").contents().find(`#txtTrainingDefaultFrame--${index}`).show();
      $(`#txtTrainingDefaultFrame--${index}`).show();
    }
  }
}
