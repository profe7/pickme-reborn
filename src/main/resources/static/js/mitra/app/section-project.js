let countSectionProject = 0;

$("#btnAddSectionProject").click(function ($e) {
  $e.preventDefault();
  countSectionProject++;
  $("#sectionProject").append(`
      <div id="sectionProjectForm--${countSectionProject}" class="border rounded py-3 col-sm-11 mt-3 section-list">
        <div id="sectionProjectCollapse--${countSectionProject}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
          data-toggle="collapse" href="#collapseProject--${countSectionProject}" role="button" aria-expanded="false"
          aria-controls="collapseProject--${countSectionProject}">
          <div class="col-md-11 col-xs-11">
            <span class="bullet"></span>
            <b id="txtProjectName--${countSectionProject}"></b>
            <span id="txtProjectSite--${countSectionProject}"></span>
            <span id="txtProjectJobDescription--${countSectionProject}"></span>
            <span id="txtProjectStartDate--${countSectionProject}"></span>
            <span id="txtProjectEndDate--${countSectionProject}"></span>
            <span id="txtProjectSkill--${countSectionProject}"></span>
            <b id="txtProjectDefault--${countSectionProject}">(Anda belum mengisi)</b>
          </div>
          <div class="col-md-1 col-xs-1">
            <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
          </div>
        </div>
        <div class="collapse container" id="collapseProject--${countSectionProject}">
          <div class="mt-4"></div>
          <div class="row">
            <div class="form-group col-md-6">
            <input type="hidden" id="inputProjectId--${countSectionProject}" type="text" oninput="autoSave()">
              <label>Nama Project</label>
              <input data-collapse="#collapseProject--${countSectionProject}"  autocomplete="off" id="inputProjectName--${countSectionProject}" type="text" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtProjectName--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectNameFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectNameFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtProjectName--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectNameFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectNameFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isProjectValueBlank(); autoSave(); validateRequire($(this), 'collapseProject')" required="">
            </div>
            <div class="form-group col-md-6">
              <label>Lembaga / Perusahaan</label>
              <input data-collapse="#collapseProject--${countSectionProject}"  id="inputProjectSite--${countSectionProject}" class="form-control required" oninput="
                if($(this).val() === ''){
                    $('#txtProjectSite--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectSiteFrame--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectSiteFrame--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtProjectSite--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectSiteFrame--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectSiteFrame--${countSectionProject}').text(' (' + $(this).val() + ') ').fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isProjectValueBlank(); validateRequire($(this), 'collapseProject'); autoSave();" required="">
            </div>
          </div>

          <div class="row" style="cursor: text !important;">
            <div class="form-group col-md-12">
              <label>Deskripsi Project</label>
              <input type="text" id="inputProjectJobDescription--${countSectionProject}" class="summernote-project">
            </div>
          </div>

          <div class="row">
            <div class="form-group col-md-6">
              <label>Mulai dari<b class="text-danger">*</b> </label>
              <input data-collapse="#collapseProject--${countSectionProject}" autocomplete="off" id="inputProjectStartDate--${countSectionProject}" type="month" class="form-control required" oninput="
              if ($(this).val() === '') {
                  $('#txtProjectStartDate--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                  $('#frame').contents().find('#txtProjectStartDateFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                  $('#txtProjectStartDateFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              } else {
                  $('#txtProjectStartDate--${countSectionProject}').text(' since ' + convertDate($(this).val()));
                  $('#frame').contents().find('#txtProjectStartDateFrame--${countSectionProject}').text(' since ' + convertDate($(this).val())).fadeTo(100, 0.3, function () { $(this).fadeTo(100, 1.0); });
                  $('#txtProjectStartDateFrame--${countSectionProject}').text(' since ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
              }
              isProjectValueBlank(); autoSave(); validateRequire($(this));" required="">
            </div>

            <div class="form-group col-md-6">
              <label>Berakhir pada <b class="text-danger">*</b> </label>
              <input data-collapse="#collapseProject--${countSectionProject}" autocomplete="off" id="inputProjectEndDate--${countSectionProject}" class="form-control required" type="month" oninput="
                if($(this).val() === ''){
                    $('#txtProjectEndDate--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectEndDateFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectEndDateFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                  $('#txtProjectEndDate--${countSectionProject}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#frame').contents().find('#txtProjectEndDateFrame--${countSectionProject}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  $('#txtProjectEndDateFrame--${countSectionProject}').text(' - ' + convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isProjectValueBlank(); autoSave(); validateRequire($(this))" required="">
            </div>
          </div>

          <div class="row">
              <div class="col-md-6"></div>
              <div class="col-md-6">
                  <div class="form-group form-check">
                      <input type="checkbox" class="form-check-input" id="projectUnEnroll--${countSectionProject}" onchange="disableEndDate($(this), '#inputProjectEndDate--${countSectionProject}', 'project')">
                      <label for="projectUnEnroll--${countSectionProject}" class="form-check-label">Sampai saat ini.</label>
                  </div>
              </div>
          </div>

          <div class="row">
            <div class="form-group col-md-12">
              <label>Skill<b class="text-danger">*</b></label>
              <select data-collapse="#collapseProject--${countSectionProject}" id="inputProjectSkill--${countSectionProject}" class="form-control required" 
                onchange="
                  if($(this).val() === ''){
                    $('#txtProjectSkill--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtProjectSkillFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtProjectSkillFrame--${countSectionProject}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                  } else {
                    $('#txtProjectSkill--${countSectionProject}').html('<b>Skills:</b> ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                    $('#frame').contents().find('#txtProjectSkillFrame--${countSectionProject}').html('<b>Skills:</b> ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                    $('#txtProjectSkillFrame--${countSectionProject}').html('<b>Skills:</b> ' + $(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); });
                  }
                isProjectValueBlank(); autoSave(); validateRequire($(this))" required="">
                <option value="" disabled seleted>-- Pilih --</option>
              </select>
            </div>
          </div>
        </div>
      </div>
      <div id="sectionProjectBtnDelete--${countSectionProject}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionProject(${countSectionProject});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>

    `);

  displayData(
    "/api/references/get-by-group_1/skills",
    `inputProjectSkill--${countSectionProject}`
  );

  $(".summernote-project").summernote({
    height: 150,
    toolbar: [
      ["font", ["bold", "underline", "clear"]],
      ["para", ["ul"]],
    ],
  });
  $(`#inputProjectJobDescription--${countSectionProject}`).summernote(
    "code",
    "<ul><li></li></ul>"
  );
  $(`#inputProjectJobDescription--${countSectionProject}`).on(
    "summernote.change",
    function (e) {
      $("#frame")
        .contents()
        .find(`#txtProjectJobDescriptionFrame--${countSectionProject}`)
        .html(
          $(`#inputProjectJobDescription--${countSectionProject}`).summernote(
            "code"
          )
        )
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      $(`#txtProjectJobDescriptionFrame--${countSectionProject}`)
        .html(
          $(`#inputProjectJobDescription--${countSectionProject}`).summernote(
            "code"
          )
        )
        .fadeTo(100, 0.3, function () {
          $(this).fadeTo(100, 1.0);
        });
      isProjectValueBlank();
      autoSave();
    }
  );

  $(`#sectionProjectForm--${countSectionProject}`).hide().fadeIn(500);

  // Frame di Live Preview sebelah kanan (sebelum diisi)
  $("#frame").contents().find("#txtProjectFrame").append(`
    <tr id="txtProjectFrame--${countSectionProject}">
        <td>
            <span class="bullet"></span>
        </td>
        <td>
            <b id="txtProjectNameFrame--${countSectionProject}"></b>
            <span id="txtProjectJobDescription--${countSectionProject}"></span>
            <span id="txtProjectSiteFrame--${countSectionProject}"></span>
            <span id="txtProjectStartDateFrame--${countSectionProject}"></span>
            <span id="txtProjectEndDateFrame--${countSectionProject}"></span>
            <b id="txtProjectDefaultFrame--${countSectionProject}">_________________________________________________</b>
            <br>
        </td>
    </tr>
    <tr id="projectJobDescriptionFrame1--${countSectionProject}">
        <td>
            <span class="ml-2"></span>
        </td>
        <td>
            <b>Job Description:</b>
        </td>
    </tr>
    <tr id="projectJobDescriptionFrame2--${countSectionProject}">
        <td>
            <span class="ml-2"></span>
        </td>
        <td class="pb-2">
            <span id="txtProjectJobDescriptionFrame--${countSectionProject}"></span>
        </td>
    </tr>
    <tr id="projectSkillFrame--${countSectionProject}">
        <td>
            <span class="ml-2"></span>
        </td>
        <td>
            <span id="txtProjectSkillFrame--${countSectionProject}"></span>
        </td>
    </tr>`);

  // Frame di dalam tombol 'Preview CV' (sebelum diisi)
  $("#frame")
    .contents()
    .find(`#txtProjectFrame--${countSectionProject}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  $("#txtProjectFrame").append(`
        <tr id="txtProjectFrame--${countSectionProject}">
          <td>
            <span class="bullet"></span>
          </td>
          <td>
          <b id="txtProjectNameFrame--${countSectionProject}"></b>
          <span id="txtProjectJobDescription--${countSectionProject}"></span>
          <span id="txtProjectSiteFrame--${countSectionProject}"></span>
          <span id="txtProjectStartDateFrame--${countSectionProject}"></span>
          <span id="txtProjectEndDateFrame--${countSectionProject}"></span>
          <b id="txtProjectDefaultFrame--${countSectionProject}">_________________________________________________</b>
      </td>
  </tr>
  <tr id="projectJobDescriptionFrame1--${countSectionProject}">
      <td>
          <span class="ml-2"></span>
      </td>
      <td>
          <b>Job Description:</b>
      </td>
  </tr>
  <tr id="projectJobDescriptionFrame2--${countSectionProject}">
      <td>
          <span class="ml-2"></span>
      </td>
      <td class="pb-2">
          <span id="txtProjectJobDescriptionFrame--${countSectionProject}"></span>
      </td>
  </tr>
  <tr id="projectSkillFrame--${countSectionProject}">
      <td>
          <span class="ml-2"></span>
      </td>
      <td>
          <span id="txtProjectSkillFrame--${countSectionProject}"></span>
      </td>
  </tr>`);
  $(`#txtProjectFrame--${countSectionProject}`).fadeTo(100, 0.3, function () {
    $(this).fadeTo(500, 1.0);
  });

  for (let i = 0; i < countSectionProject; i++) {
    let index = i + 1;

    if ($(`#collapseProject--${index}`).hasClass("collapse show")) {
      $(`#sectionProjectCollapse--${index}`).click();
    }
  }

  isProjectValueBlank();
  renderAutocomplete();
});

function deletesectionProject(id) {
  // countSectionProject--;
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
          $(`#sectionProjectForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionProjectBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtProjectFrame--${id}`)
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
            "/api/delete-user-project/" + $(`#inputProjectId--${id}`).val()
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

function isProjectValueBlank() {
  for (var i = 0; i < countSectionProject; i++) {
    var index = i + 1;
    if (
      !$(`#inputProjectJobDescription--${index}`).summernote("isEmpty") ||
      $(`#inputProjectStartDate--${index}`).val() !== "" ||
      $(`#inputProjectEndDate--${index}`).val() !== "" ||
      $(`#inputProjectName--${index}`).val() !== "" ||
      $(`#inputProjectSkill--${index}`).val() !== ""
    ) {
      $(`#txtProjectDefault--${index}`).hide();
      $("#frame").contents().find(`#txtProjectDefaultFrame--${index}`).hide();
      $(`#txtProjectDefaultFrame--${index}`).hide();
    } else {
      $(`#txtProjectDefault--${index}`).show();
      $("#frame").contents().find(`#txtProjectDefaultFrame--${index}`).show();
      $(`#txtProjectDefaultFrame--${index}`).show();
    }
    if (
      ($(`#inputProjectJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
        $(`#inputProjectJobDescription--${index}`)
          .summernote("code")
          .includes("<ul><li><br></li></ul>")) &&
      $(`#inputProjectStartDate--${index}`).val() == "" &&
      $(`#inputProjectEndDate--${index}`).val() == "" &&
      $(`#inputProjectName--${index}`).val() == "" &&
      $(`#inputProjectSkill--${index}`).val() == ""
    ) {
      $(`#txtProjectDefault--${index}`).show();
    }
    if (
      $(`#inputProjectJobDescription--${index}`).summernote("isEmpty") ||
      $(`#inputProjectJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li></li></ul>") ||
      $(`#inputProjectJobDescription--${index}`)
        .summernote("code")
        .includes("<ul><li><br></li></ul>")
    ) {
      $("#frame")
        .contents()
        .find(`#projectJobDescriptionFrame1--${index}`)
        .hide();
      $("#frame")
        .contents()
        .find(`#projectJobDescriptionFrame2--${index}`)
        .hide();
      $(`#projectJobDescriptionFrame1--${index}`).hide();
      $(`#projectJobDescriptionFrame2--${index}`).hide();
    } else {
      $("#frame")
        .contents()
        .find(`#projectJobDescriptionFrame1--${index}`)
        .show();
      $("#frame")
        .contents()
        .find(`#projectJobDescriptionFrame2--${index}`)
        .show();
      $(`#projectJobDescriptionFrame1--${index}`).show();
      $(`#projectJobDescriptionFrame2--${index}`).show();
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
