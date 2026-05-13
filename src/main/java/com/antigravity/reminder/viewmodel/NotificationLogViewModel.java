package com.antigravity.reminder.viewmodel;

import com.antigravity.reminder.dao.NotificationLogDao;
import com.antigravity.reminder.model.NotificationLog;
import java.util.List;

public class NotificationLogViewModel {
    private final NotificationLogDao logDao = new NotificationLogDao();

    public List<NotificationLog> getLogs() {
        return logDao.getAll();
    }
}
