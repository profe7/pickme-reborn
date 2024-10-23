const postData = async (url, data) => {
  const result = await $.ajax({
    url: url,
    type: "POST",
    data: data,
    dataType: "json",
    contentType: "application/json; charset=utf-8",
  });

  return result;
};
const blobData = async (url, data) => {
  const result = await $.ajax({
    url: url,
    method: "POST",
    timeout: 0,
    processData: false,
    mimeType: "multipart/form-data",
    contentType: false,
    data: data,
  });
  return result;
};

const getData = async (url) => {
  const result = await $.ajax({
    url: url,
    type: "GET",
    dataType: "json",
  });

  return result;
};

const deleteData = async (url) => {
  const result = await $.ajax({
    url: url,
    type: "Delete",
  });

  return result;
};

const exportData = async (url, data) => {
  const result = await $.ajax({
    url: url,
    method: "POST",
    data: data,
    contentType: "application/json",
    xhrFields: {
      responseType: "blob",
    },
  });

  return result;
};
