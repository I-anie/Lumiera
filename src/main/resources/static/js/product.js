document.addEventListener("DOMContentLoaded", () => {
    const addCartForm = document.querySelector("#add-cart-form");
    if (!addCartForm) return;

    addCartForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const productId = document.querySelector("#productId").value;
        const quantity = document.querySelector("#quantity").value;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;

        try {
            const response = await fetch(`${window.contextPath}/api/cart/items`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({
                    productId: Number(productId),
                    quantity: Number(quantity)
                })
            });

            const contentType = response.headers.get("content-type");

            if (!contentType || !contentType.includes("application/json")) {
                if (response.redirected) {
                    window.location.href = response.url;
                    return;
                }

                if (response.status === 401 || response.status === 403) {
                    alert("로그인이 필요합니다.");
                    window.location.href = `${window.contextPath}/login`;
                    return;
                }

                const text = await response.text();
                console.error(text);
                throw new Error("JSON이 아닌 응답이 반환되었습니다.");
            }

            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(result.message || "장바구니 추가 실패");
            }

            alert(result.message || "장바구니에 추가되었습니다.");
        } catch (error) {
            console.error(error);
            alert(error.message || "장바구니 추가에 실패했습니다.");
        }
    });
});