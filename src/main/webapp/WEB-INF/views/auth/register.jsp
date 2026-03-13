<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/base.jsp" %>

<html lang="ko">
<head>
    <title>회원가입</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="${contextPath}/css/style.css" rel="stylesheet">
    <link href="${contextPath}/css/auth.css" rel="stylesheet">
</head>
<body>

<%@ include file="/WEB-INF/views/common/index.jsp" %>

<div class="page-container page-narrow">
    <div class="content-card">
        <div class="card-section auth-card">
            <h1 class="auth-title">회원가입</h1>
            <p class="auth-subtitle">
                이미 회원이신가요?
                <a href="${loginUrl}" class="inline-link">로그인</a>
            </p>

            <form:form modelAttribute="registerForm" action="${registerUrl}" method="post">
                <form:errors path="" element="div" cssClass="global-error"/>

                <div class="mb-3">
                    <label for="username" class="form-label">아이디</label>
                    <form:input path="username" id="username" cssClass="form-control" placeholder="아이디를 입력하세요"/>
                    <form:errors path="username" element="div" cssClass="field-error"/>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">비밀번호</label>
                    <form:password path="password" id="password" cssClass="form-control" placeholder="비밀번호를 입력하세요"/>
                    <form:errors path="password" element="div" cssClass="field-error"/>
                </div>

                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">비밀번호 확인</label>
                    <form:password path="confirmPassword" id="confirmPassword" cssClass="form-control"
                                   placeholder="비밀번호를 다시 입력하세요"/>
                    <form:errors path="confirmPassword" element="div" cssClass="field-error"/>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary auth-submit">회원가입</button>
            </form:form>
        </div>
    </div>
</div>

<div class="app-footer">
    © Lumiera Official Online Shop
</div>
</body>
</html>