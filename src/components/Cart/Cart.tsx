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
    userId: number;
};

const Cart: React.FC<Props> = ({cartItems, addToCart, removeFromCart, userId}) => {
    const calculateTotal = (items: CartItemType[]) =>
        items.reduceRight(
            (accumulator: number, item) => {
                console.log(item.discount, (accumulator + item.amount * item.price * (1 - item.discount) / 100).toFixed(2));
                return +((accumulator + item.amount * item.price * (1 - item.discount) / 100).toFixed(2));
            }, 0
        );

    async function handleCheckout() {
        const header = {
            userId: userId
        }
        const response = await axios({
            url: 'http://localhost:8080/orders',
            method: 'post',
            headers: header
        });

        for (const [index, obj] of Object.entries(response.data)) {
            for(const [key, value] of Object.entries(obj)){
                console.log(`${key}: ${value}`);
            }
        }
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
