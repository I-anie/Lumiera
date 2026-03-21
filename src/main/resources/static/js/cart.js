document.addEventListener("DOMContentLoaded", () => {
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    async function requestJson(url, method, body) {
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                [csrfHeader]: csrfToken
            },
            body: body ? JSON.stringify(body) : null
        });

        const result = await response.json();

        if (!response.ok || !result.success) {
            throw new Error(result.message || "요청 실패");
        }

        return result;
    }

    document.querySelectorAll('[id^="update-cart-item-"]').forEach((button) => {
        button.addEventListener("click", async () => {
            const itemId = button.id.replace("update-cart-item-", "");
            const productId = Number(document.querySelector(`#productId-${itemId}`).value);
            const quantity = Number(document.querySelector(`#quantity-${itemId}`).value);

            try {
                const result = await requestJson(
                    `${window.cartItemPath}/${itemId}`,
                    "PATCH",
                    {productId, quantity}
                );

                alert(result.message);
                location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });

    document.querySelectorAll('[id^="delete-cart-item-"]').forEach((button) => {
        button.addEventListener("click", async () => {
            const itemId = button.id.replace("delete-cart-item-", "");

            try {
                const result = await requestJson(
                    `${window.cartItemPath}/${itemId}`,
                    "DELETE",
                    null
                );

                alert(result.message);
                location.reload();
            } catch (error) {
                alert(error.message);
            }
        });
    });
});