let countSectionWork = 0;
let workNumber = 0;
$("#btnAddSectionWork").click(function ($e) {
  $e.preventDefault();
  countSectionWork++;
  workNumber++;
  $("#sectionWork").append(`
      <div id="sectionWorkForm--${countSectionWork}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionWorkCollapse--${countSectionWork}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseWork--${countSectionWork}" role="button" aria-expanded="false"
          aria-controls="collapseWork--${countSectionWork}">
          <div class="col-md-11 col-xs-11">
          <span class="bullet"></span>
          <span id="txtWorkStartDate--${countSectionWork}"></span>
          <span id="txtWorkEndDate--${countSectionWork}"></span>
          <b id="txtPosition--${countSectionWork}"></b>
          <b id="txtCompanyName--${countSectionWork}"></b>
          <b id="txtWorkDefault--${countSectionWork}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseWork--${countSectionWork}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-6">            
              <input type="hidden" id="inputWorkId--${countSectionWork}">
                <label>Perusahaan </label>
                <input data-collapse="#collapseWork--${countSectionWork}" autocomplete="off" id="inputCompanyName--${countSectionWork}" type="text" class="form-control required" oninput="
                  if($(this).val() === ''){
                      $('#txtCompanyName--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtCompanyNameFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtCompanyNameFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  } else {
                      $('#txtCompanyName--${countSectionWork}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtCompanyNameFrame--${countSectionWork}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtCompanyNameFrame--${countSectionWork}').text(' at ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  }
                      isWorkValueBlank(); autoSave(); validateRequire($(this))" required="">          
            </div>
            <div class="form-group col-md-6">
              <label>Posisi</label>
              <select data-collapse="#collapseWork--${countSectionWork}" id="inputPosition--${countSectionWork}" class="form-control required" onchange="
                if($(this).val() === ''){
                    $('#txtPosition--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtPositionFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtPositionFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtPosition--${countSectionWork}').text(' as ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtPositionFrame--${countSectionWork}').text(' as ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtPositionFrame--${countSectionWork}').text(' as ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isWorkValueBlank(); autoSave(); validateRequire($(this))" required="">
                <option value="" disabled selected>-- Pilih --</option>
              </select>
            </div>
          </div>
          
          <div class="row">
            <div class="form-group form-check col-md-6 d-flex align-items-end">
                <input type="checkbox" class="form-check-input" id="workUnEnroll--${countSectionWork}" onchange="disableEndDate($(this), '#inputWorkEndDate--${countSectionWork}', 'work')" style="margin-left: 0!important;">
                <label for="workUnEnroll--${countSectionWork}" class="form-check-label" style="margin-left: 20px!important;">Saya masih berkerja disini</label>
            </div>
            <div class="form-group col-md-6">
              <label>Status Contract</label>
              <select data-collapse="#collapseWork--${countSectionWork}" id="selectWorkStatus--${countSectionWork}" class="form-control required" onchange="
                if($(this).val() === ''){
                      $('#txtWorkStatus--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtWorkStatusFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtWorkStatusFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  } else {
                      $('#txtWorkStatus--${countSectionWork}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#frame').contents().find('#txtWorkStatusFrame--${countSectionWork}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                      $('#txtWorkStatusFrame--${countSectionWork}').text('('+$(this).find('option:selected').text()+')').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  }
                isWorkValueBlank(); autoSave(); validateRequire($(this))" required="">
                <option value="" disabled selected>-- Pilih --</option>
                <option value="INTERNSHIP">Internship</option>
                <option value="CONTRACT">Contract</option>
                <option value="PART_TIME">Part Time</option>
                <option value="FULL_TIME">Full Time</option>
              </select>
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6">
            <label>Dimulai dari </label>
              <input data-collapse="#collapseWork--${countSectionWork}" autocomplete="off" id="inputWorkStartDate--${countSectionWork}" type="month" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtWorkStartDate--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtWorkStartDateFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtWorkStartDateFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtWorkStartDate--${countSectionWork}').text(' ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtWorkStartDateFrame--${countSectionWork}').text(' ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtWorkStartDateFrame--${countSectionWork}').text(' ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                $('#inputWorkEndDate--${countSectionWork}').attr('min', $(this).val())
                isWorkValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
            <div class="form-group col-md-6">   
            <label>Berakhir pada</label>
              <input data-collapse="#collapseWork--${countSectionWork}" autocomplete="off" id="inputWorkEndDate--${countSectionWork}" type="month" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtWorkEndDate--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtWorkEndDateFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtWorkEndDateFrame--${countSectionWork}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtWorkEndDate--${countSectionWork}').text(' - ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtWorkEndDateFrame--${countSectionWork}').text(' - ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtWorkEndDateFrame--${countSectionWork}').text(' - ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                    isWorkValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>
          <div class="row" style="cursor: text !important;">
            <div class="form-group col-md-12">
              <label>Deskripsi Pekerjaan</label>
              <input type="text" id="inputJobDescription--${countSectionWork}" class="summernote-work">
            </div>
          </div>
          <div class="row" style="cursor: text !important;">
            <div class="form-group col-md-12">
              <label>Spesifikasi Proyek</label>
              <input type="text" id="inputProjectSpesification--${countSectionWork}" class="summernote-work">
            </div>
          </div>
        </div>
      </div>
      <div id="sectionWorkBtnDelete--${countSectionWork}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deleteSectionWork(${countSectionWork});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);

  displayData(
    "/api/references/get-by-group_1/posisi_pekerjaan",
    `inputPosition--${countSectionWork}`
  );

  $(".summernote-work").summernote({
    height: 150,
    toolbar: [
      ["font", ["bold", "underline", "clear"]],
      ["para", ["ul"]],
    ],
  });
  $(`#inputJobDescription--${countSectionWork}`).summernote(
    "code",
    "<ul><li></li></ul>"
  );
  $(`#inputProjectSpesification--${countSectionWork}`).summernote(
    "code",
    "<ul><li></li></ul>"
  );
  $(`#inputJobDescription--${countSectionWork}`).on(
    "summernote.change",
    function (e) {
      $("#frame")
        .contents()
        .find(`#txtJobDescriptionFrame--${countSectionWork}`)
        .html($(`#inputJobDescription--${countSectionWork}`).summernote("code"))
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      $(`#txtJobDescriptionFrame--${countSectionWork}`)
        .html($(`#inputJobDescription--${countSectionWork}`).summernote("code"))
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      isWorkValueBlank();
      autoSave();
    }
  );

  $(`#inputProjectSpesification--${countSectionWork}`).on(
    "summernote.change",
    function (e) {
      $("#frame")
        .contents()
        .find(`#txtProjectSpesificationFrame--${countSectionWork}`)
        .html(
          $(`#inputProjectSpesification--${countSectionWork}`).summernote(
            "code"
          )
        )
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      $(`#txtProjectSpesificationFrame--${countSectionWork}`)
        .html(
          $(`#inputProjectSpesification--${countSectionWork}`).summernote(
            "code"
          )
        )
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      isWorkValueBlank();
      autoSave();
    }
  );

  $(`#sectionWorkForm--${countSectionWork}`).hide().fadeIn(500);

  $("#frame").contents().find("#txtWorkFrame").append(`
        <tr id="txtWorkFrame--${countSectionWork}">
            <td>
                <span id="workNumber--${workNumber}">${workNumber}&period;&nbsp;</span>
            </td>
            <td class="pb-2">
                <span id="txtWorkStartDateFrame--${countSectionWork}"></span>
                <span id="txtWorkEndDateFrame--${countSectionWork}"></span>
                <b id="txtPositionFrame--${countSectionWork}"></b>
                <b id="txtCompanyNameFrame--${countSectionWork}"></b>
                <span id="txtWorkStatusFrame--${countSectionWork}"></span>
                <b id="txtWorkDefaultFrame--${countSectionWork}">_________________________________________________</b>
            </td>
        </tr>
        <tr id="workJobDescriptionFrame1--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td>
                <b>Job Description:</b>
            </td>
        </tr>
        <tr id="workJobDescriptionFrame2--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td class="pb-2">
                <span id="txtJobDescriptionFrame--${countSectionWork}"></span>
            </td>
        </tr>
        <tr id="workProjectSpecificationFrame1--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td>
                <b>Project Specification:</b>
            </td>
        </tr>
        <tr id="workProjectSpecificationFrame2--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td class="pb-2">
                <span id="txtProjectSpesificationFrame--${countSectionWork}"></span>
            </td>
        </tr>`);
  $("#frame")
    .contents()
    .find(`#txtWorkFrame--${countSectionWork}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtWorkFrame").append(`
        <tr id="txtWorkFrame--${countSectionWork}">
            <td>
                <span id="workNumber--${workNumber}">${workNumber}&period;&nbsp;</span>
            </td>
            <td class="pb-2">
                <b id="txtPositionFrame--${countSectionWork}"></b>
                <b id="txtCompanyNameFrame--${countSectionWork}"></b>
                <span id="txtWorkStartDateFrame--${countSectionWork}"></span>
                <span id="txtWorkEndDateFrame--${countSectionWork}"></span>
                <span id="txtWorkStatusFrame--${countSectionWork}"></span>
                <b id="txtWorkDefaultFrame--${countSectionWork}">_________________________________________________</b>
            </td>
        </tr>
        <tr id="workJobDescriptionFrame1--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td>
                <b>Job Description:</b>
            </td>
        </tr>
        <tr id="workJobDescriptionFrame2--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td class="pb-2">
                <span id="txtJobDescriptionFrame--${countSectionWork}"></span>
            </td>
        </tr>
        <tr id="workProjectSpecificationFrame1--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td>
                <b>Project Specification:</b>
            </td>
        </tr>
        <tr id="workProjectSpecificationFrame2--${countSectionWork}">
            <td>
                <span class="ml-2"></span>
            </td>
            <td class="pb-2">
                <span id="txtProjectSpesificationFrame--${countSectionWork}"></span>
            </td>
        </tr>`);
  $(`#txtWorkFrame--${countSectionWork}`).fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

  for (let i = 0; i < countSectionWork; i++) {
    let index = i + 1;

    if ($(`#collapseWork--${index}`).hasClass("collapse show")) {
      $(`#sectionWorkCollapse--${index}`).click();
    }
  }
  isWorkValueBlank();
  renderAutocomplete();
});

function deleteSectionWork(id) {
  // countSectionWork--;
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
          $(`#sectionWorkForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionWorkBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtWorkFrame--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          $(".modal-body")
            .contents()
            .find(`#txtWorkFrame--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");

          $("#frame").contents().find(`#workNumber--${id}`).remove();
          $(".modal-body").contents().find(`#workNumber--${id}`).remove();
          var frameArray = [];
          var mbArray = [];
          for (var i = 1; i <= workNumber; i++) {
            if ($("#frame").contents().find(`#workNumber--${i}`).length) {
              frameArray.push($("#frame").contents().find(`#workNumber--${i}`));
              mbArray.push(
                $(".modal-body").contents().find(`#workNumber--${i}`)
              );
            }
          }
          for (var i = 0; i < frameArray.length; i++) {
            var el = frameArray[i];
            var el2 = mbArray[i];
            el.prop("id", `workNumber--${i + 1}`).text(i + 1);
            el2.prop("id", `workNumber--${i + 1}`).text(i + 1);
          }
          workNumber = frameArray.length;
          deleteData("/api/delete-user-work/" + $(`#inputWorkId--${id}`).val())
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

function isWorkValueBlank() {
  for (var i = 0; i < countSectionWork; i++) {
    var index = i + 1;
    if (
      !$(`#inputJobDescription--${index}`).summernote("isEmpty") ||
      !$(`#inputProjectSpesification--${index}`).summernote("isEmpty") ||
      $(`#inputWorkStartDate--${index}`).val() !== "" ||
      $(`#inputWorkEndDate--${index}`).val() !== "" ||
      $(`#inputCompanyName--${index}`).val() !== "" ||
      $(`#inputPosition--${index}`).val() !== ""
    ) {
      $(`#txtWorkDefault--${index}`).hide();
      $("#frame").contents().find(`#txtWorkDefaultFrame--${index}`).hide();
      $(`#txtWorkDefaultFrame--${index}`).hide();
    } else {
      $(`#txtWorkDefault--${index}`).show();
      $("#frame").contents().find(`#txtWorkDefaultFrame--${index}`).show();
      $(`#txtWorkDefaultFrame--${index}`).show();
    }
    if (
      ($(`#inputJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
        $(`#inputJobDescription--${index}`)
          .summernote("code")
          .includes("<ul><li><br></li></ul>")) &&
      ($(`#inputProjectSpesification--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
        $(`#inputProjectSpesification--${index}`)
          .summernote("code")
          .includes("<ul><li><br></li></ul>")) &&
      $(`#inputWorkStartDate--${index}`).val() == "" &&
      $(`#inputWorkEndDate--${index}`).val() == "" &&
      $(`#inputCompanyName--${index}`).val() == "" &&
      $(`#inputPosition--${index}`).val() == ""
    ) {
      $(`#txtWorkDefault--${index}`).show();
    }
    if (
      $(`#inputJobDescription--${index}`).summernote("isEmpty") ||
      $(`#inputJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
      $(`#inputJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li><br></li></ul>")
    ) {
      $("#frame").contents().find(`#workJobDescriptionFrame1--${index}`).hide();
      $("#frame").contents().find(`#workJobDescriptionFrame2--${index}`).hide();
      $(`#workJobDescriptionFrame1--${index}`).hide();
      $(`#workJobDescriptionFrame2--${index}`).hide();
    } else {
      $("#frame").contents().find(`#workJobDescriptionFrame1--${index}`).show();
      $("#frame").contents().find(`#workJobDescriptionFrame2--${index}`).show();
      $(`#workJobDescriptionFrame1--${index}`).show();
      $(`#workJobDescriptionFrame2--${index}`).show();
    }

    if (
      $(`#inputProjectSpesification--${index}`).summernote("isEmpty") ||
      $(`#inputProjectSpesification--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
      $(`#inputProjectSpesification--${index}`)
        .summernote("code")
        .includes("<ul><li><br></li></ul>")
    ) {
      $("#frame")
        .contents()
        .find(`#workProjectSpecificationFrame1--${index}`)
        .hide();
      $("#frame")
        .contents()
        .find(`#workProjectSpecificationFrame2--${index}`)
        .hide();
      $(`#workProjectSpecificationFrame1--${index}`).hide();
      $(`#workProjectSpecificationFrame2--${index}`).hide();
    } else {
      $("#frame")
        .contents()
        .find(`#workProjectSpecificationFrame1--${index}`)
        .show();
      $("#frame")
        .contents()
        .find(`#workProjectSpecificationFrame2--${index}`)
        .show();
      $(`#workProjectSpecificationFrame1--${index}`).show();
      $(`#workProjectSpecificationFrame2--${index}`).show();
    }
  }
}

function displayData(url, selectId) {
  $.ajax({
    type: "GET",
    url: url,
    dataType: "json",
    success: function (response) {
      var data = response;
      var select = $("#" + selectId);

      select.find('option:not([value=""])').remove();
      // select.empty().append('<option value="" disabled selected>-- Pilih --</option>');

      data.forEach(function (reference) {
        var option = $("<option></option>")
          .attr("value", reference.id)
          .text(reference.reference_name);
        select.append(option);
      });
    },
    error: function (error) {
      console.error("Error fetching data:", error);
    },
  });
}
