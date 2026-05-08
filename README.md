# Instrument Shop Java Project

## **List of ~12 possible actions:**
- Add product to list of products for sale
- Remove a product from the list of products for sale
- Apply a bulk discount
- Generate low-stock report
- View inventory valuation
- Register shop partner
- Remove shop partner
- Sell a product to a partner 
- Update the information of a product
- Filter products 
- Start a new sale period 
- End a sale period
- Get information about a sale period
- Create CSV file with sale information about a period

## **List of ~10 entities**
- Guitar
- Amplifier
- Drumset
- Keyboard
- Piano
- Bass
- Accessory
- Partner
- SaleRecord
- SalePeriod
- StoreInventory
- User

## **Entity Relations**
- Inheritance for instruments:
    - `Superclass`: Product
    - `Subclasses`: Guitar, Amplifier, Drumset, Keyboard, Piano, Bass
- Composition for sales:
    - **SalePeriod** has many **SaleRecords**
    - **SaleRecord** has one **Product** and one **Partner**
    - **StoreInventory** has many **Products**