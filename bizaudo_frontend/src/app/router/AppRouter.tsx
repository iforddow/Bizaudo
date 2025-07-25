import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import HomePage from "../pages/HomePage";
import AuthBanner from "../../features/auth/components/AuthBanner";
import AuthPage from "../../features/auth/pages/AuthPage";
import CompleteProfileModal from "../../features/user/components/modals/CompleteProfileModal";
import CompleteProfilePage from "../../features/user/pages/CompleteProfilePage";

/* 
The AppRouter component defines the main routing structure of the application.
It uses React Router to manage navigation between different pages.

@author IFD
@since 2025-06-27
*/
export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />

        {/* Authentication Routes */}
        <Route path="/auth" element={<AuthPage />} />
        <Route
          path="/login"
          element={<Navigate to="/auth?login=true" replace />}
        />
        <Route path="/signup" element={<Navigate to="/auth" replace />} />
        {/* User Profile Routes */}
        <Route>
          <Route path="/complete-profile" element={<CompleteProfilePage />} />
        </Route>
      </Routes>
      <CompleteProfileModal />
      <AuthBanner />
    </BrowserRouter>
  );
}
