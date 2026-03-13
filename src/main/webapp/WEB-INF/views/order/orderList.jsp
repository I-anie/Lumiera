<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/base.jsp" %>

<html lang="ko">
<head>
    <title>주문 내역</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link href="${contextPath}/css/style.css" rel="stylesheet">
    <link href="${contextPath}/css/order.css" rel="stylesheet">
</head>
<body>

<%@ include file="/WEB-INF/views/common/index.jsp" %>

<div class="page-container">
    <div class="page-header">
        <h1 class="page-title">주문 내역</h1>
        <p class="page-subtitle">주문한 상품과 진행 상태를 확인해보세요.</p>
    </div>

    <sec:authorize access="hasRole('ADMIN')">
        <c:if test="${not empty message}">
            <div class="alert alert-info" role="alert">
                    ${message}
            </div>
        </c:if>
    </sec:authorize>

    <div class="content-card mb-4">
        <div class="card-section">
            <form:form modelAttribute="searchCondition" action="${orderUrl}" method="get" cssClass="order-search-form">
                <input type="hidden" name="page" value="1">

                <sec:authorize access="hasRole('ADMIN')">
                    <form:select path="deleted" cssClass="form-select">
                        <form:option value="false" label="정상"/>
                        <form:option value="true" label="삭제"/>
                    </form:select>

                    <form:input path="username" type="text" cssClass="form-control" placeholder="주문자"/>
                </sec:authorize>

                <form:select path="size" cssClass="form-select">
                    <form:option value="10" label="10개"/>
                    <form:option value="20" label="20개"/>
                    <form:option value="30" label="30개"/>
                </form:select>

                <div class="date-range">
                    <form:input path="startDate" type="date" cssClass="form-control"/>
                    <span>~</span>
                    <form:input path="endDate" type="date" cssClass="form-control"/>
                </div>

                <form:select path="status" cssClass="form-select">
                    <form:option value="" label="전체 상태"/>
                    <form:option value="PENDING" label="결제 대기"/>
                    <form:option value="CONFIRMED" label="주문 확인"/>
                    <form:option value="PREPARING" label="상품 준비"/>
                </form:select>

                <button type="submit" class="btn btn-primary">검색</button>
            </form:form>
        </div>
    </div>

    <c:choose>
        <c:when test="${empty orderList}">
            <div class="content-card">
                <div class="card-section">
                    <div class="empty-box">
                        <div class="empty-box-title">주문 내역이 없습니다</div>
                        <p class="mb-3">아직 주문한 상품이 없어요. 원하는 상품을 둘러보세요.</p>
                        <a href="${productUrl}" class="btn btn-primary">상품 보러 가기</a>
                    </div>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <sec:authorize access="hasRole('ADMIN')">
                <form action="${orderUrl}/preparing" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                    <div class="bulk-action-bar">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="checkAll">
                            <label class="form-check-label" for="checkAll">전체 선택</label>
                        </div>

                        <button type="submit" class="btn btn-primary">상품 준비로 변경</button>
                    </div>

                    <div class="order-list">
                        <c:forEach items="${orderList}" var="order">
                            <c:url var="orderDetailUrl" value="${orderUrl}/${order.id}"/>

                            <div class="order-card">
                                <div class="order-card-body">
                                    <div class="order-card-top">
                                        <div class="d-flex align-items-start gap-3">
                                            <div class="form-check mt-1">
                                                <input class="form-check-input order-checkbox"
                                                       type="checkbox"
                                                       name="orderIds"
                                                       value="${order.id}"
                                                       id="order-${order.id}">
                                            </div>

                                            <div>
                                                <div class="data-label">주문 번호</div>
                                                <div class="data-value">#${order.id}</div>
                                            </div>
                                        </div>

                                        <div class="text-end">
                                            <div class="data-label">주문 상태</div>
                                            <span class="badge-status">${order.status}</span>
                                        </div>
                                    </div>

                                    <div class="order-card-divider"></div>

                                    <div class="order-info-grid">
                                        <div>
                                            <div class="data-label">주문 금액</div>
                                            <div class="data-value">${order.totalPrice}원</div>
                                        </div>
                                        <div>
                                            <div class="data-label">주문일</div>
                                            <div class="data-value">${order.createdAt}</div>
                                        </div>
                                        <div class="d-flex align-items-end justify-content-md-end">
                                            <a href="${orderDetailUrl}" class="order-detail-link">상세보기</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </form>

                <script>
                    document.addEventListener("DOMContentLoaded", function () {
                        const checkAll = document.getElementById("checkAll");
                        const checkboxes = document.querySelectorAll(".order-checkbox");

                        if (checkAll) {
                            checkAll.addEventListener("change", function () {
                                checkboxes.forEach(function (checkbox) {
                                    checkbox.checked = checkAll.checked;
                                });
                            });
                        }

                        checkboxes.forEach(function (checkbox) {
                            checkbox.addEventListener("change", function () {
                                const allChecked = Array.from(checkboxes).every(cb => cb.checked);
                                const anyChecked = Array.from(checkboxes).some(cb => cb.checked);

                                checkAll.checked = allChecked;
                                checkAll.indeterminate = !allChecked && anyChecked;
                            });
                        });
                    });
                </script>
            </sec:authorize>

            <sec:authorize access="hasRole('USER')">
                <div class="order-list">
                    <c:forEach items="${orderList}" var="order">
                        <c:url var="orderDetailUrl" value="${orderUrl}/${order.id}"/>
                        <a href="${orderDetailUrl}" class="order-card-link">
                            <div class="order-card">
                                <div class="order-card-body">
                                    <div class="order-card-top">
                                        <div>
                                            <div class="data-label">주문 번호</div>
                                            <div class="data-value">#${order.id}</div>
                                        </div>

                                        <div class="text-end">
                                            <div class="data-label">주문 상태</div>
                                            <span class="badge-status">${order.status}</span>
                                        </div>
                                    </div>

                                    <div class="order-card-divider"></div>

                                    <div class="order-info-grid">
                                        <div>
                                            <div class="data-label">주문 금액</div>
                                            <div class="data-value">${order.totalPrice}원</div>
                                        </div>
                                        <div>
                                            <div class="data-label">주문일</div>
                                            <div class="data-value">${order.createdAt}</div>
                                        </div>
                                        <div class="d-flex align-items-end justify-content-md-end">
                                            <span class="order-detail-link">상세보기</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </sec:authorize>

            <div class="pagination-wrap">
                <ul class="pagination mb-0">
                    <c:if test="${pageHandler.hasPrev}">
                        <c:url var="prevUrl" value="${orderUrl}">
                            <c:param name="page" value="${pageHandler.startPage - 1}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="startDate" value="${searchCondition.startDate}"/>
                            <c:param name="endDate" value="${searchCondition.endDate}"/>
                            <c:param name="status" value="${searchCondition.status}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="username" value="${searchCondition.username}"/>
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>
                        <li class="page-item"><a class="page-link" href="${prevUrl}">Previous</a></li>
                    </c:if>

                    <c:forEach begin="${pageHandler.startPage}" end="${pageHandler.endPage}" var="page">
                        <c:url var="pageUrl" value="${orderUrl}">
                            <c:param name="page" value="${page}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="startDate" value="${searchCondition.startDate}"/>
                            <c:param name="endDate" value="${searchCondition.endDate}"/>
                            <c:param name="status" value="${searchCondition.status}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="username" value="${searchCondition.username}"/>
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>

                        <li class="page-item ${page == searchCondition.page ? 'active' : ''}">
                            <a class="page-link" href="${pageUrl}">${page}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${pageHandler.hasNext}">
                        <c:url var="nextUrl" value="${orderUrl}">
                            <c:param name="page" value="${pageHandler.endPage + 1}"/>
                            <c:param name="size" value="${searchCondition.size}"/>
                            <c:param name="startDate" value="${searchCondition.startDate}"/>
                            <c:param name="endDate" value="${searchCondition.endDate}"/>
                            <c:param name="status" value="${searchCondition.status}"/>
                            <sec:authorize access="hasRole('ADMIN')">
                                <c:param name="username" value="${searchCondition.username}"/>
                                <c:param name="deleted" value="${searchCondition.deleted}"/>
                            </sec:authorize>
                        </c:url>

                        <li class="page-item"><a class="page-link" href="${nextUrl}">Next</a></li>
                    </c:if>
                </ul>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<div class="app-footer">
    © Lumiera Official Online Shop
</div>
</body>
</html>