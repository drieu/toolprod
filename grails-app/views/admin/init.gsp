<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title>Upload New Document</title>
</head>
<body>
<div class="content scaffold-create" role="main">
    <h1>Upload Your apps list</h1>
    <g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
    <g:uploadForm action="upload" controller="admin">
        <fieldset class="form">
            <input type="file" name="appLst" />
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="upload" class="save" value="Upload" />
        </fieldset>
    </g:uploadForm>
</div>
</body>
</html>