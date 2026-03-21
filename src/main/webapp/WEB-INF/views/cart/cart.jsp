<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/base.jsp" %>

<html lang="ko">
<head>
    <title>장바구니</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">

    <script src="${contextPath}/js/cart.js"></script>
    <script>
        window.contextPath = "${contextPath}";
        window.cartItemPath = window.contextPath + "/api/cart/items"
    </script>

    <link href="${contextPath}/css/style.css" rel="stylesheet">
    <link href="${contextPath}/css/cart.css" rel="stylesheet">
</head>
<body>

<%@ include file="/WEB-INF/views/common/index.jsp" %>

<div class="page-container">
    <div class="page-header">
        <h1 class="page-title">장바구니</h1>
        <p class="page-subtitle">주문할 상품을 확인하고 수량을 조정해보세요.</p>
    </div>

    <div class="content-card">
        <div class="card-section">
            <c:choose>
                <c:when test="${empty cartItems}">
                    <div class="empty-box">
                        <div class="empty-box-title">장바구니가 비어 있습니다</div>
                        <p class="mb-0">원하는 상품을 담아 주문을 시작해보세요.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-wrap">
                        <table class="table table-clean">
                            <thead>
                            <tr>
                                <th style="width: 52px;">
                                    <input type="checkbox" id="checkAll">
                                </th>
                                <th style="width: 100px;">상품</th>
                                <th>상품명</th>
                                <th style="width: 140px;">가격</th>
                                <th style="width: 220px;">수량 변경</th>
                                <th style="width: 100px;">삭제</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:url var="productUrl" value="${contextPath}/products"/>
                            <c:forEach items="${cartItems}" var="cartItem">
                                <tr>
                                    <td>
                                        <input type="checkbox"
                                               name="cartItemIds"
                                               value="${cartItem.id}"
                                               class="cart-item-checkbox"
                                               form="orderForm">
                                    </td>

                                    <td>
                                        <a href="${productUrl}/${cartItem.productId}">
                                            <img src="${cartItem.thumbnailUrl}" class="thumbnail-sm"
                                                 alt="${cartItem.name}">
                                        </a>
                                    </td>

                                    <td>
                                        <a href="${productUrl}/${cartItem.productId}" class="cart-product-name">
                                                ${cartItem.name}
                                        </a>
                                    </td>

                                    <td class="price-text">${cartItem.price}원</td>

                                    <td>
                                        <input type="hidden"
                                               id="productId-${cartItem.id}"
                                               value="${cartItem.productId}">

                                        <input type="number"
                                               id="quantity-${cartItem.id}"
                                               value="${cartItem.quantity}"
                                               min="1"
                                               class="form-control cart-quantity-input">

                                        <button type="button"
                                                id="update-cart-item-${cartItem.id}"
                                                class="btn btn-soft">
                                            변경
                                        </button>
                                    </td>

                                    <td>
                                        <button type="button"
                                                id="delete-cart-item-${cartItem.id}"
                                                class="btn btn-outline-danger">
                                            삭제
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="cart-footer">
                        <p class="cart-note mb-0">체크한 상품만 주문됩니다.</p>

                        <form id="orderForm" action="${orderUrl}/create" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            <button type="button" class="btn btn-primary">선택 상품 주문</button>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    const checkAll = document.getElementById('checkAll');
    const itemCheckboxes = document.querySelectorAll('.cart-item-checkbox');

    if (checkAll) {
        checkAll.addEventListener('change', function () {
            itemCheckboxes.forEach(function (checkbox) {
                checkbox.checked = checkAll.checked;
            });
        });

        itemCheckboxes.forEach(function (checkbox) {
            checkbox.addEventListener('change', function () {
                const checkedCount = document.querySelectorAll('.cart-item-checkbox:checked').length;
                checkAll.checked = itemCheckboxes.length > 0 && checkedCount === itemCheckboxes.length;
            });
        });
    }
</script>

<div class="app-footer">
    © Lumiera Official Online Shop
</div>
</body>
</html>