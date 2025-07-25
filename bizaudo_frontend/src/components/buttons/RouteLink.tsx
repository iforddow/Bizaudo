import { Link } from "react-router-dom";

export default function RouteLink({
  children,
  to,
}: {
  children: React.ReactNode;
  to: string;
}) {
  return (
    <Link to={to} style={{ all: "unset" }}>
      {children}
    </Link>
  );
}
