let sectionData;

const initTemplate = () => {
  let profileName = readCookie("userName").replaceAll(`"`, "");
  let profilePhoto = readCookie("profilePicture").replaceAll(`"`, "");
  let el = `<img alt="image" id="profilePhoto" src="${profilePhoto}" class="profile-picture mr-auto">
                <div class="d-sm-none d-lg-inline-block" id="profileName">${profileName}</div>`;
  $(".nav-link-user").append(el);

  $('[data-toggle="popover"]').popover();
};

const initTable = () => {
  $("#sortable tbody").sortable({
    cursor: "move",
    placeholder: "sortable-placeholder",
    helper: function (e, tr) {
      var $originals = tr.children();
      var $helper = tr.clone();
      $helper.children().each(function (index) {
        $(this).width($originals.eq(index).width());
      });
      return $helper;
    },
    update: function (event, ui) {
      updateOrder();
    },
  });
};

const initData = async () => {
  $(".loading-spinner").show();
  sectionList = getData("/api/section").then((data) => {
    $("#sortable tbody").empty();
    data.forEach((section, index) => {
      $("#sortable tbody").append(`
                    <tr id="${section.id}">
                        <td class="text-center"><i class="fa fa-bars"></i></td>
                        <td>${index + 1}</td>
                        <td>${section.name}</td>
                        <td><a href="javascript:void(0)" class="btn btn-primary btn-sm btn-block mb-2 btn-round"
                            onclick="openEditModal('${section.id}', '${
        section.name
      }', ${index + 1})">
                            <i class="fas fa-pen"></i> Edit
                        </a>
                    </tr>`);
    });
    $(".loading-spinner").hide();
    return data;
  });
  sectionData = sectionList;
};

const openEditModal = (id, value, order) => {
  $("#formEdit").trigger("reset");
  $("#sectionId").val(id + "-section-name");
  $("#sectionName").val(value);
  $("#sectionOrder").val("Ke-" + order);
  $("#modalEdit").modal("show");
};

const editData = () => {
  let data = {
    id: $("#sectionId").val(),
    name: $("#sectionName").val(),
  };

  $(".loading-spinner").show();
  $("#modalEdit").modal("hide");

  postData("/api/section", JSON.stringify(data)).then(() => {
    initData();
  });
};

const updateOrder = () => {
  let data = [];
  $("#sortable tbody tr").each(function (index, element) {
    let id = $(element).attr("id");
    let order = index + 1;
    data.push({
      id: id + "-section-order",
      order: order,
    });
  });

  console.log(data);

  $(".loading-spinner").show();
  postData("/api/section/order", JSON.stringify(data)).then(() => {
    initData();
  });
};

const readCookie = (name) => {
  let nameEQ = name + "=";
  let ca = document.cookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == " ") c = c.substring(1, c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length, c.length);
  }
  return null;
};

$(document).ready(function () {
  initTemplate();
  initTable();
  initData();
});
