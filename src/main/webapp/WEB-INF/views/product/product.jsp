<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/base.jsp" %>

<html lang="ko">
<head>
    <title>상품 상세</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="${contextPath}/css/style.css" rel="stylesheet">
    <link href="${contextPath}/css/product.css" rel="stylesheet">
</head>
<body>

<%@ include file="/WEB-INF/views/common/index.jsp" %>

<div class="page-container">
    <div class="content-card mb-4">
        <div class="card-section">
            <div class="product-detail-grid">
                <div>
                    <div class="product-thumbnail-box">
                        <img src="${product.thumbnailUrl}" alt="${product.name}" class="product-thumbnail-lg">
                    </div>
                </div>

                <div>
                    <div class="product-summary-top">
                        <span class="category-chip">${product.type}</span>
                    </div>

                    <h1 class="product-title">${product.name}</h1>
                    <div class="product-price">${product.price}원</div>

                    <div class="info-panel">
                        <div class="info-row">
                            <div class="info-key">상품 ID</div>
                            <div class="info-value">${product.id}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-key">카테고리</div>
                            <div class="info-value">${product.type}</div>
                        </div>
                        <div class="info-row">
                            <div class="info-key">재고 수량</div>
                            <div class="info-value">${product.stockQuantity}개</div>
                        </div>
                    </div>

                    <sec:authorize access="hasRole('ADMIN')">
                        <div class="info-panel">
                            <h2 class="section-title">관리자 정보</h2>
                            <div class="info-row">
                                <div class="info-key">생성일</div>
                                <div class="info-value">${product.createdAt}</div>
                            </div>
                            <div class="info-row">
                                <div class="info-key">수정일</div>
                                <div class="info-value">${product.updatedAt}</div>
                            </div>
                            <div class="info-row">
                                <div class="info-key">삭제일</div>
                                <div class="info-value">${product.deletedAt}</div>
                            </div>
                        </div>
                    </sec:authorize>

                    <sec:authorize access="!hasRole('ADMIN')">
                        <div class="purchase-box">
                            <form:form modelAttribute="cartItem"
                                       action="${contextPath}/cart/add"
                                       method="post">

                                <form:input path="productId" value="${product.id}" type="hidden"/>

                                <div class="mb-3">
                                    <label class="form-label" for="quantity">수량</label>
                                    <form:input path="quantity" id="quantity" type="number" min="1"
                                                cssClass="form-control"/>
                                    <form:errors path="quantity" element="div" cssClass="field-error"/>
                                </div>

                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                                <div class="d-grid">
                                    <button type="submit" class="btn btn-primary">장바구니 추가</button>
                                </div>
                            </form:form>
                        </div>
                    </sec:authorize>
                </div>
            </div>
        </div>
    </div>

    <div class="content-card">
        <div class="card-section">
            <h2 class="section-title">상세 이미지</h2>

            <c:choose>
                <c:when test="${not empty detailImages}">
                    <div class="detail-image-list">
                        <c:forEach var="detailImage" items="${detailImages}">
                            <div class="detail-image-box">
                                <img src="${detailImage.imageUrl}" alt="상세 이미지" class="detail-image">
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-box">
                        <div class="empty-box-title">상세 이미지가 없습니다</div>
                        <p class="mb-0">등록된 상세 이미지가 없습니다.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<div class="app-footer">
    © Lumiera Official Online Shop
</div>
</body>
</html>