let countSectionAward = 0;

$("#btnAddSectionAward").click(function ($e) {
  $e.preventDefault();
  countSectionAward++;
  $("#sectionAward").append(`
      <div id="sectionAwardForm--${countSectionAward}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionAwardCollapse--${countSectionAward}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseAward--${countSectionAward}" role="button" aria-expanded="false"
          aria-controls="collapseAward--${countSectionAward}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtAwardName--${countSectionAward}"></b>
            <span id="txtAwardInstitute--${countSectionAward}"></span>
            <span id="txtAwardYear--${countSectionAward}"></span>
            <b id="txtAwardDefault--${countSectionAward}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseAward--${countSectionAward}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-12">
              <input type="hidden" id="inputAwardId--${countSectionAward}">
              <label>Nama Penghargaan/Prestasi </label>
              <input data-collapse="#collapseAward--${countSectionAward}" autocomplete="autocomplete_off_cv_award" id="inputAwardName--${countSectionAward}" type="text" class="form-control required" onkeyup="
                $('#txtAwardName--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                $('#frame').contents().find('#txtAwardNameFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                $('#txtAwardNameFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                isAwardValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-8">
              <label>lembaga </label>
              <input data-collapse="#collapseAward--${countSectionAward}" autocomplete="off" id="inputAwardInstitute--${countSectionAward}" type="text" class="form-control required" onkeyup="
                if($(this).val() === ''){
                    $('#txtAwardInstitute--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtAwardInstituteFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtAwardInstituteFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtAwardInstitute--${countSectionAward}').text(' from '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtAwardInstituteFrame--${countSectionAward}').text(' from '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtAwardInstituteFrame--${countSectionAward}').text(' from '+$(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isAwardValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
            <div class="form-group col-md-4">
              <label>Tahun </label>
              <input data-collapse="#collapseAward--${countSectionAward}" autocomplete="off" id="inputAwardYear--${countSectionAward}" type="number" pattern="\\d*" maxlength="4" min="2000" max="3000" 
                step="1" placeholder="----" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtAwardYear--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtAwardYearFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtAwardYearFrame--${countSectionAward}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtAwardYear--${countSectionAward}').text('('+$(this).val()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtAwardYearFrame--${countSectionAward}').text('('+$(this).val()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtAwardYearFrame--${countSectionAward}').text('('+$(this).val()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isAwardValueBlank(); autoSave(); validateRequire($(this)); validateYear($(this))">
            </div>
          </div>
        </div>
      </div>
      <div id="sectionAwardBtnDelete--${countSectionAward}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionAward(${countSectionAward});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);

  $(`#sectionAwardForm--${countSectionAward}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtAwardFrame").append(`
        <tr id="txtListAward--${countSectionAward}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtAwardNameFrame--${countSectionAward}"></b>
            <span id="txtAwardInstituteFrame--${countSectionAward}"></span>
            <span id="txtAwardYearFrame--${countSectionAward}"></span>
            <b id="txtAwardDefaultFrame--${countSectionAward}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtListAward--${countSectionAward}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtAwardFrame").append(`
        <tr id="txtListAward--${countSectionAward}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
            <b id="txtAwardNameFrame--${countSectionAward}"></b>
            <span id="txtAwardInstituteFrame--${countSectionAward}"></span>
            <span id="txtAwardYearFrame--${countSectionAward}"></span>
            <b id="txtAwardDefaultFrame--${countSectionAward}">_________________________________________________</b>
            <br>
          </td>
        </tr>`);
  $(`#txtListAward--${countSectionAward}`).fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

  for (let i = 0; i < countSectionAward; i++) {
    let index = i + 1;

    if ($(`#collapseAward--${index}`).hasClass("collapse show")) {
      $(`#sectionAwardCollapse--${index}`).click();
    }
  }

  isAwardValueBlank();
  renderAutocomplete();
});

function deletesectionAward(id) {
  // countSectionAward--;

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
          $(`#sectionAwardForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionAwardBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtListAward--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");

          deleteData(
            "/api/delete-user-award/" + $(`#inputAwardId--${id}`).val()
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

function isAwardValueBlank() {
  for (var i = 0; i < countSectionAward; i++) {
    var index = i + 1;
    if (
      $(`#inputAwardName--${index}`).val() !== "" ||
      $(`#inputAwardYear--${index}`).val() !== ""
    ) {
      $(`#txtAwardDefault--${index}`).hide();
      $("#frame").contents().find(`#txtAwardDefaultFrame--${index}`).hide();
      $(`#txtAwardDefaultFrame--${index}`).hide();
    } else {
      $(`#txtAwardDefault--${index}`).show();
      $("#frame").contents().find(`#txtAwardDefaultFrame--${index}`).show();
      $(`#txtAwardDefaultFrame--${index}`).show();
    }
  }
}
