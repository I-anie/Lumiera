<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/base.jsp" %>

<html lang="ko">
<head>
    <title>상품 목록</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="${contextPath}/css/style.css" rel="stylesheet">
    <link href="${contextPath}/css/product.css" rel="stylesheet">
</head>
<body>

<%@ include file="/WEB-INF/views/common/index.jsp" %>

<div class="page-container">
    <div class="page-header">
        <h1 class="page-title">상품 목록</h1>
        <p class="page-subtitle">원하는 상품을 검색하고 상세 정보를 확인해보세요.</p>
    </div>

    <div class="content-card">
        <div class="card-section">
            <div class="product-toolbar">
                <sec:authorize access="hasRole('ADMIN')">
                    <button type="button"
                            class="btn btn-primary"
                            onclick="location.href='${productUrl}/create'">
                        상품 등록
                    </button>
                </sec:authorize>

                <form:form modelAttribute="searchCondition"
                           action="${productUrl}"
                           method="get"
                           cssClass="product-search-form">

                    <input type="hidden" name="page" value="1"/>

                    <sec:authorize access="hasRole('ADMIN')">
                        <select name="deleted" class="form-select">
                            <option value="">전체</option>
                            <option value="false" ${searchCondition.deleted == false ? 'selected' : ''}>정상</option>
                            <option value="true" ${searchCondition.deleted == true ? 'selected' : ''}>삭제</option>
                        </select>
                    </sec:authorize>

                    <form:select path="size" cssClass="form-select">
                        <form:option value="10" label="10개"/>
                        <form:option value="20" label="20개"/>
                        <form:option value="30" label="30개"/>
                    </form:select>

                    <form:input path="keyword"
                                cssClass="form-control keyword-input"
                                placeholder="상품명을 입력하세요"/>

                    <form:select path="categoryId" cssClass="form-select">
                        <form:option value="" label="전체 카테고리"/>
                        <c:forEach items="${categoryList}" var="category">
                            <form:option value="${category.id}" label="${category.type}"/>
                        </c:forEach>
                    </form:select>

                    <button type="submit" class="btn btn-primary">검색</button>
                </form:form>
            </div>

            <div class="table-wrap">
                <table class="table table-clean">
                    <thead>
                    <tr>
                        <th style="width: 90px;">ID</th>
                        <th style="width: 100px;">대표 이미지</th>
                        <th style="width: 140px;">카테고리</th>
                        <th>상품명</th>
                        <th style="width: 140px;">가격</th>
                        <th style="width: 140px;">재고 수량</th>
                        <sec:authorize access="hasRole('ADMIN')">
                            <th style="width: 180px;">생성일</th>
                        </sec:authorize>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${empty productList}">
                            <tr>
                                <td colspan="7">
                                    <div class="empty-box">
                                        <div class="empty-box-title">검색 결과가 없습니다</div>
                                        <p class="mb-0">검색 조건을 변경해서 다시 확인해보세요.</p>
                                    </div>
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${productList}" var="product">
                                <c:url var="productDetailUrl" value="${productUrl}/${product.id}"/>
                                <tr>
                                    <td class="fw-semibold">#${product.id}</td>
                                    <td>
                                        <a href="${productDetailUrl}">
                                            <img src="${product.thumbnailUrl}" class="thumbnail-sm" alt="${product.name}">
                                        </a>
                                    </td>
                                    <td>
                                        <span class="category-chip">${product.type}</span>
                                    </td>
                                    <td>
                                        <a href="${productDetailUrl}" class="product-name-link">
                                                ${product.name}
                                        </a>
                                    </td>
                                    <td class="price-text">${product.price}원</td>
                                    <td class="stock-text">${product.stockQuantity}개</td>
                                    <sec:authorize access="hasRole('ADMIN')">
                                        <td>${product.createdAt}</td>
                                    </sec:authorize>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <div class="pagination-wrap">
                <ul class="pagination mb-0">
                    <c:if test="${pageHandler.hasPrev}">
                        <c:url var="prevUrl" value="${productUrl}">
                            <c:param name="page" value="${pageHandler.startPage - 1}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="keyword" value="${searchCondition.keyword}"/>
                            <c:param name="categoryId" value="${searchCondition.categoryId}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>
                        <li class="page-item"><a class="page-link" href="${prevUrl}">Previous</a></li>
                    </c:if>

                    <c:forEach begin="${pageHandler.startPage}" end="${pageHandler.endPage}" var="page">
                        <c:url var="pageUrl" value="${productUrl}">
                            <c:param name="page" value="${page}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="keyword" value="${searchCondition.keyword}"/>
                            <c:param name="categoryId" value="${searchCondition.categoryId}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>
                        <li class="page-item ${page == searchCondition.page ? 'active' : ''}">
                            <a class="page-link" href="${pageUrl}">${page}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${pageHandler.hasNext}">
                        <c:url var="nextUrl" value="${productUrl}">
                            <c:param name="page" value="${pageHandler.endPage + 1}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="keyword" value="${searchCondition.keyword}"/>
                            <c:param name="categoryId" value="${searchCondition.categoryId}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>
                        <li class="page-item"><a class="page-link" href="${nextUrl}">Next</a></li>
                    </c:if>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="app-footer">
    © Lumiera Official Online Shop
</div>
</body>
</html>