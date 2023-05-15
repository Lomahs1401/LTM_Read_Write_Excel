<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Excel Processing</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
          integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N" crossorigin="anonymous">
</head>
<body>
<div style="margin-top: 30px;">
    <h1 style="text-align: center">
        Thi Lập Trình Mạng
    </h1>
    <br/>
    <form id="fileForm" action="${pageContext.request.contextPath}/read-excel" method="POST"
          enctype="multipart/form-data" onsubmit="readExcel(event)">
        <div style="display: flex; justify-content: center; gap: 20px; align-items: center">
            <button class="btn btn-primary" type="button" onclick="document.getElementById('fileInput').click();">
                <input class="btn btn-primary" type="file" name="file" id="fileInput" style="display: none"/>
                <p style="margin: 0">Chọn file excel</p>
            </button>
            <input type="submit" class="btn btn-primary" value="Đọc File">
            <button class="btn btn-primary" type="button" onclick="readTeacher()">
                <p style="margin: 0">Đọc dữ liệu danh sách cán bộ</p>
            </button>
            <button class="btn btn-primary" type="button" onclick="readExaminationRoom()">
                <p style="margin: 0">Đọc dữ liệu danh sách phòng thi</p>
            </button>
            <button class="btn btn-success" type="button" onclick="shuffleData()">
                <p style="margin: 0">Xáo trộn ca thi</p>
            </button>
            <button class="btn btn-secondary" type="button" onclick="writeExcel()">
                <p style="margin: 0">Xuất file excel</p>
            </button>
        </div>
    </form>
    <br/>
</div>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj"
        crossorigin="anonymous">
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
        integrity="sha512-894YE6QWD5I59HgZOGReFYm4dnWc1Qt5NtvYSaNcOP+u1T9qYdvdihz0PPSiiqn/+/3e7Jo4EaG7TubfWGUrMQ=="
        crossorigin="anonymous"
        referrerpolicy="no-referrer">
</script>
<script src="index.js"></script>
</body>
</html>