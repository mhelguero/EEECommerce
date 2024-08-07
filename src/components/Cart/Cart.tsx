import CartItem from "../CartItem/CartItem";

// Styles
import {Wrapper} from "./Cart.styles";

// Types
import {CartItemType} from "../../types";
import {StyledButton} from "../../App.styles.ts";
import axios from "axios";

type Props = {
    cartItems: CartItemType[];
    addToCart: (clickedItem: CartItemType) => void;
    removeFromCart: (id: number) => void;
};

const Cart: React.FC<Props> = ({cartItems, addToCart, removeFromCart}) => {
    const calculateTotal = (items: CartItemType[]) =>
        items.reduceRight(
            (accumulator: number, item) => accumulator + item.amount * item.price / 100,
            0
        );

    async function handleCheckout() {
        const header = {
            userType: 'CUSTOMER',
            userId: 1
        }
        const {result} = await axios({
            url: 'http://localhost:8080/orders',
            method: 'post',
            headers: header
        });
    }

    return (
        <Wrapper>
            <h2>Shopping Cart</h2>
            {cartItems.length === 0 ? <p>No Items in cart.</p> : null}
            {cartItems.map((item) => (
                <CartItem
                    key={item.id}
                    item={item}
                    addToCart={addToCart}
                    removeFromCart={removeFromCart}
                />
            ))}
            <h2>Total: ${calculateTotal(cartItems).toFixed(2)}</h2>
            <StyledButton onClick={handleCheckout}>Checkout</StyledButton>
        </Wrapper>
    );
};

export default Cart;
