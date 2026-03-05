# 🧠 Why Add Service Layer?

Because:
Business logic belongs in Service
Controller should only handle HTTP
Later: stock rules, discounts, ordering logic, validation

In a supermarket system you will have:
Check stock before ordering
Reduce stock after order
Prevent negative quantity
Apply discounts
Calculate totals
This logic must NOT be inside Controller.

-------------------------------

# 🛒 Now Think Big Picture

Your final system architecture:

                    ┌───────────────┐
                    │   MySQL DB    │
                    └──────▲────────┘
                           │
                    ┌──────┴────────┐
                    │ Spring Boot   │
                    │ Backend API   │
                    └──────▲────────┘
               ┌───────────┴───────────┐
               │                       │
        JavaFX Desktop App     Kotlin Android App
        

One backend.
Multiple clients.

That’s scalable architecture.

------------------------------

# 🎯 Big Picture — What Did We Just Build?

You now have:

## 1️⃣ Backend (Server)

Built with:
Spring Boot

It:
Connects to MySQL
Stores products
Exposes REST API
Returns JSON

## 2️⃣ Desktop App (Client)

Built with:
OpenJFX

It:
Shows UI
Calls backend
Displays data
