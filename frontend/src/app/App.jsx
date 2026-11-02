import { BrowserRouter } from 'react-router-dom';
import { AppProvider } from '../entities/estado';
import { AppRoutes } from './routes';
export default function App() {
    return (<BrowserRouter>
      <AppProvider>
        <AppRoutes />
      </AppProvider>
    </BrowserRouter>);
}
