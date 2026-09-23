# Bazaari — Marketplace Frontend (UI demo)

A single-file, self-contained HTML/CSS/JS reference UI for the Marketplace backend.
Open `index.html` directly in a browser — no build step required.

## What's in it

- **Shop view** — hero, category rail, product grid with quick-add, slide-in cart drawer, login/register modal
- **Sell view** — seller dashboard: revenue/orders/stock stats, product table, incoming orders table
- **Admin view** — platform stats, pending seller approvals, recent orders across all sellers

Switch between the three with the pill toggle in the top utility bar — this mirrors the
Customer / Seller / Admin roles from the backend's `User.role` field.

## Wiring it to the real API

Right now all data (`PRODUCTS`, `sellerProducts`, `sellerOrders`, etc.) is hard-coded in
`<script>` for demo purposes. To connect it to the Spring Boot backend, replace those
arrays with `fetch()` calls against the endpoints in the root README, e.g.:

```js
async function loadProducts(){
  const res = await fetch("http://localhost:8080/api/products/index");
  const { data } = await res.json();
  return data; 
}
```

You'll also need to enable CORS on the backend (`@CrossOrigin` on the routers/controllers,
or a global `CorsConfigurationSource` bean) since the frontend and API run on different
ports during local development.
