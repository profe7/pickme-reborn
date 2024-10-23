let countSectionEducation = 0;

$("#btnAddSectionEducation").click(function ($e) {
  $e.preventDefault();
  countSectionEducation++;
  $("#sectionEducation").append(`
    <div id="sectionEducationForm--${countSectionEducation}" class="border rounded py-3 col-sm-11 mt-3 section-list">
      <div id="sectionEducationCollapse--${countSectionEducation}" class="row justify-content-around collapsed align-middle" style="cursor: pointer;"
           data-toggle="collapse" href="#collapseEducation--${countSectionEducation}" role="button" aria-expanded="false"
           aria-controls="collapseEducation--${countSectionEducation}">
        <div class="col-md-11 col-xs-11">
          <span class="bullet"></span>
          <b id="txtEducationPosition--${countSectionEducation}"></b>
          <b id="txtEducationName--${countSectionEducation}"></b>
          <span id="txtEducationStartDate--${countSectionEducation}"></span>
          <span id="txtEducationEndDate--${countSectionEducation}"></span>
          <b id="txtEducationDefault--${countSectionEducation}">(Anda belum mengisi)</b>
        </div>
        <div class="col-md-1 col-xs-1">
          <i class="fa mt-1 text-primary pl-4" aria-hidden="true"></i>
        </div>
      </div>
      <div class="collapse container" id="collapseEducation--${countSectionEducation}">
        <div class="mt-4"></div>
        <div class="row">
          <div class="form-group col-md-6">
            <input type="hidden" id="inputEducationId--${countSectionEducation}">
            <label>Universitas / Sekolah</label>
            <input autocomplete="autocomplete_off_cv_org_name" id="inputEducationName--${countSectionEducation}" type="text" class="form-control required" required="">
          </div>
            <div class="form-group col-md-6">
              <label>Jenjang</label>
              <select id="inputEducationPosition--${countSectionEducation}" class="form-control required" required="" onchange="toggleJenjangDropdown(${countSectionEducation})">
                 <option value="" disabled selected>-- Pilih --</option>
                 <option value="SD">SD</option>
                 <option value="SMP">SMP</option>
                 <option value="SMA">SMA</option>
                 <option value="SMK">SMK</option>
                 <option value="D_III">D-III</option>
                 <option value="D_IV">D-IV</option>
                 <option value="S1">S1</option>
                 <option value="S2">S2</option>
                 <option value="S3">S3</option>
              </select>
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6" id="selectMajor--${countSectionEducation}" style="display: none;">
              <label>Jurusan</label>
              <select id="selectUniversityMajor--${countSectionEducation}" class="form-control required" required="">
                <option value="" disabled selected>-- Pilih --</option>
              </select>
              <select id="selectVHSMajor--${countSectionEducation}" class="form-control required" required="">
                <option value="" disabled selected>-- Pilih --</option>
              </select>
            </div>            
            <div class="form-group col-md-6" id="scoreField--${countSectionEducation}" style="display: none;">
              <label>Nilai</label>
              <input id="inputEducationScore--${countSectionEducation}" type="number" step="1" class="form-control required" required="" min="0" max="100" oninput="validateScore(this)">
            </div>
            <div class="form-group col-md-6" id="gpaField--${countSectionEducation}" style="display: none;">
              <label>IPK</label>
              <input id="inputEducationGPA--${countSectionEducation}" type="number" step="0.1" class="form-control required" required="" min="0" max="4.0" oninput="validateScore(this)">
            </div>
          </div>
          <div class="row">
            <div class="form-group form-check ml-3">
                <input type="checkbox" id="EducationUnEnroll--${countSectionEducation}" class="form-check-input">
                <label for="EducationUnEnroll--${countSectionEducation}" class="form-check-label">Saya masih bersekolah disini</label>
            </div>
          </div>
          <div class="row">
            <div class="form-group col-md-6">
              <label>Dimulai dari</label>
              <input id="inputEducationStartDate--${countSectionEducation}" type="month" class="form-control required" required="">
            </div>
            <div class="form-group col-md-6">
             <label>Berakhir pada</label>
              <input data-collapse="#collapseEducation--${countSectionEducation}" id="inputEducationEndDate--${countSectionEducation}" class="form-control required" type="month" oninput="
                if($(this).val() === ''){
                    $('#txtEducationEndDate--${countSectionEducation}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtEducationEndDateFrame--${countSectionEducation}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtEducationEndDateFrame--${countSectionEducation}').text($(this).val()).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                } else {
                    $('#txtEducationEndDate--${countSectionEducation}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#frame').contents().find('#txtEducationEndDateFrame--${countSectionEducation}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                    $('#txtEducationEndDateFrame--${countSectionEducation}').text(' - '+convertDate($(this).val())).fadeTo(100, 0.3, function() { $(this).fadeTo(100, 1.0); })
                }
                isEducationValueBlank(); autoSave(); validateRequire($(this))" required="">            </div>
          </div>
        </div>
      </div>
     <div id="sectionEducationBtnDelete--${countSectionEducation}" class="row col-sm-1 mt-4">
        <a id="btn" href="javascript:void(0)" class="text-muted delete ml-4 mt-2" data-toggle="tooltip" data-placement="top" title="Hapus"
          onclick="deletesectionEducation(${countSectionEducation});">
          <i class="text-bottom fa fa-trash"></i>
        </a>
      </div>
    `);

  $(`#sectionEducationForm--${countSectionEducation}`).hide().fadeIn(500);

  validateGPA(countSectionEducation);
  displayData(
    "/api/references/get-by-group_1/university_major",
    `selectUniversityMajor--${countSectionEducation}`
  );
  displayData(
    "/api/references/get-by-group_1/vhs_major",
    `selectVHSMajor--${countSectionEducation}`
  );

  // Event listener Validation GPA Inputted
  $(`#inputEducationPosition--${countSectionEducation}`).change(function () {
    validateGPA(countSectionEducation);
  });

  // Event listener for input fields
  $(document).on(
    "input",
    `#inputEducationName--${countSectionEducation}`,
    function () {
      const inputValue = $(this).val();
      if (inputValue.trim() !== "") {
        $(`#txtEducationDefault--${countSectionEducation}`).hide();
        $(`#txtEducationName--${countSectionEducation}`).text(inputValue);
      } else {
        $(`#txtEducationDefault--${countSectionEducation}`).show();
        $(`#txtEducationName--${countSectionEducation}`).text("");
      }
    }
  );

  // Event listener for checkbox
  $(document).on(
    "change",
    `#EducationUnEnroll--${countSectionEducation}`,
    function () {
      const isChecked = $(this).is(":checked");
      const endDateInput = $(
        `#inputEducationEndDate--${countSectionEducation}`
      );
      if (isChecked) {
        endDateInput.prop("disabled", true).val("");
      } else {
        endDateInput.prop("disabled", false);
      }
    }
  );

  // Frame di Live Preview sebelah kanan (sebelum diisi)
  $("#frame").contents().find("#txtEducationFrame").append(`
  <tr id="txtEducationFrame--${countSectionEducation}">
    <td>
      <span class="bullet"></span>
    </td>
    <td>
      <b id="txtEducationNameFrame--${countSectionEducation}"></b>
      <span id="txtEducationInstituteFrame--${countSectionEducation}"></span>
      <span id="txtEducationReleaseDateFrame--${countSectionEducation}"></span>
      <span id="txtEducationDateFrame--${countSectionEducation}"></span>
      <b id="txtEducationDefaultFrame--${countSectionEducation}">_________________________________________________</b>
      <br>
    </td>
  </tr>`);

  $("#frame")
    .contents()
    .find(`#txtEducationFrame--${countSectionEducation}`)
    .fadeTo(100, 0.3, function () {
      $(this).fadeTo(500, 1.0);
    });

  // Frame di dalam tombol 'Preview CV' (sebelum diisi)
  $("#txtEducationFrame").append(`
  <tr id="txtEducationFrame--${countSectionEducation}">
    <td>
      <span class="bullet"></span>
    </td>
    <td>
      <b id="txtEducationNameFrame--${countSectionEducation}"></b>
      <span id="txtEducationInstituteFrame--${countSectionEducation}"></span>
      <span id="txtEducationReleaseDateFrame--${countSectionEducation}"></span>
      <span id="txtEducationDateFrame--${countSectionEducation}"></span>
      <b id="txtEducationDefaultFrame--${countSectionEducation}">_________________________________________________</b>
      <br>
    </td>
  </tr>`);

  $(`#txtEducationFrame--${countSectionEducation}`).fadeTo(
    100,
    0.3,
    function () {
      $(this).fadeTo(500, 1.0);
    }
  );

  // Collapse handling
  for (let i = 1; i <= countSectionEducation; i++) {
    if ($(`#collapseEducation--${i}`).hasClass("collapse show")) {
      $(`#sectionEducationCollapse--${i}`).click();
    }
  }

  isEducationValueBlank();
  renderAutocomplete();
});

function toggleJenjangDropdown(count) {
  const selectJenjang = document.getElementById(
    `inputEducationPosition--${count}`
  );
  const gpaField = document.getElementById(`gpaField--${count}`);
  const scoreField = document.getElementById(`scoreField--${count}`);
  const selectMajor = document.getElementById(`selectMajor--${count}`);
  const universityMajor = document.getElementById(
    `selectUniversityMajor--${count}`
  );
  const vhs_Major = document.getElementById(`selectVHSMajor--${count}`);

  if (
    selectJenjang.value === "SD" ||
    selectJenjang.value === "SMP" ||
    selectJenjang.value === "SMA"
  ) {
    scoreField.style.display = "block";
    gpaField.style.display = "none";

    selectMajor.style.display = "none";
    vhs_Major.style.display = "none";
    universityMajor.style.display = "none";
  } else if (selectJenjang.value === "SMK") {
    scoreField.style.display = "block";
    gpaField.style.display = "none";

    selectMajor.style.display = "block";
    vhs_Major.style.display = "block";
    universityMajor.style.display = "none";
  } else if (
    selectJenjang.value === "D_III" ||
    selectJenjang.value === "D_IV" ||
    selectJenjang.value === "S1" ||
    selectJenjang.value === "S2" ||
    selectJenjang.value === "S3"
  ) {
    gpaField.style.display = "block";
    scoreField.style.display = "none";

    selectMajor.style.display = "block";
    vhs_Major.style.display = "none";
    universityMajor.style.display = "block";
  } else {
    gpaField.style.display = "none";
    scoreField.style.display = "none";

    selectMajor.style.display = "none";
    vhs_Major.style.display = "none";
    universityMajor.style.display = "none";
  }
}

function deletesectionEducation(id) {
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
          $(`#sectionEducationForm--${id}`).fadeOut(400, function () {
            $(this).remove();
            autoSave();
          });
          $(`#sectionEducationBtnDelete--${id}`).fadeOut(400, function () {
            $(this).remove();
          });
          $("#frame")
            .contents()
            .find(`#txtEducationFrame--${id}`)
            .fadeOut(400, function () {
              $(this).remove();
            });
          instance.hide({ transitionOut: "fadeOut" }, toast, "button");

          deleteData(
            `/api/delete-user-Education/${$(`#inputEducationId--${id}`).val()}`
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

function isEducationValueBlank() {
  for (var i = 0; i < countSectionEducation; i++) {
    var index = i + 1;
    if (
      $(`#inputEducationName--${index}`).val() !== "" ||
      $(`#inputEducationInstitute--${index}`).val() !== "" ||
      $(`#inputEducationReleaseDate--${index}`).val() !== "" ||
      $(`#inputEducationValidityPeriod--${index}`).val() !== ""
    ) {
      $(`#txtEducationDefault--${index}`).hide();
      $("#frame").contents().find(`#txtEducationDefaultFrame--${index}`).hide();
      $(`#txtEducationDefaultFrame--${index}`).hide();
    } else {
      $(`#txtEducationDefault--${index}`).show();
      $("#frame").contents().find(`#txtEducationDefaultFrame--${index}`).show();
      $(`#txtEducationDefaultFrame--${index}`).show();
    }
  }
}

function validateGPA(input) {
  if (input.value > 4.0) {
    alert("Nilai IPK tidak boleh lebih dari 4.0");
    input.value = "";
  }
}

function validateScore(input) {
  if (input.value > 100) {
    alert("Nilai tidak boleh lebih dari 100");
    input.value = "";
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
