<template>
  <div class="admin-roles">
    <div class="page-header">
      <h1>🔐 角色权限管理</h1>
    </div>
    
    <div class="tabs">
      <button class="tab" :class="{ active: activeTab === 'roles' }" @click="activeTab = 'roles'">角色管理</button>
      <button class="tab" :class="{ active: activeTab === 'users' }" @click="activeTab = 'users'">用户管理</button>
      <button class="tab" :class="{ active: activeTab === 'permissions' }" @click="activeTab = 'permissions'">权限配置</button>
    </div>

    <div class="card">
      <table class="table" v-if="activeTab === 'roles'">
        <thead>
          <tr>
            <th>角色名称</th>
            <th>角色编码</th>
            <th>描述</th>
            <th>用户数</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="role in roles" :key="role.id">
            <td>{{ role.name }}</td>
            <td><code>{{ role.code }}</code></td>
            <td>{{ role.description }}</td>
            <td>{{ role.userCount }}</td>
            <td>
              <button class="btn btn-sm" @click="editRole(role)">编辑</button>
              <button class="btn btn-sm btn-default" @click="configurePermissions(role)">配置权限</button>
            </td>
          </tr>
        </tbody>
      </table>

      <table class="table" v-if="activeTab === 'users'">
        <thead>
          <tr>
            <th>用户名</th>
            <th>姓名</th>
            <th>角色</th>
            <th>组织</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.name }}</td>
            <td><span class="tag tag-primary">{{ user.role }}</span></td>
            <td>{{ user.org }}</td>
            <td><span class="tag" :class="user.status === 'ACTIVE' ? 'tag-success' : 'tag-default'">{{ user.status }}</span></td>
            <td>
              <button class="btn btn-sm" @click="editUser(user)">编辑</button>
            </td>
          </tr>
        </tbody>
      </table>

      <table class="table" v-if="activeTab === 'permissions'">
        <thead>
          <tr>
            <th>权限名称</th>
            <th>权限编码</th>
            <th>类型</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="perm in permissions" :key="perm.id">
            <td>{{ perm.name }}</td>
            <td><code>{{ perm.code }}</code></td>
            <td><span class="tag tag-default">{{ perm.type }}</span></td>
            <td>
              <button class="btn btn-sm" @click="editPermission(perm)">编辑</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'AdminRoles',
  setup() {
    const activeTab = ref('roles')
    
    const roles = ref([
      { id: 1, name: '管理员', code: 'ADMIN', description: '系统管理员', userCount: 3 },
      { id: 2, name: '企业购买方', code: 'BUYER', description: '企业购买方用户', userCount: 15 },
      { id: 3, name: '产品提供方', code: 'PROVIDER', description: '产品提供方用户', userCount: 8 },
      { id: 4, name: '运营人员', code: 'OPERATOR', description: '平台运营人员', userCount: 5 }
    ])

    const users = ref([
      { id: 1, username: 'admin', name: '系统管理员', role: '管理员', org: '平台总部', status: 'ACTIVE' },
      { id: 2, username: 'buyer1', name: '张总', role: '企业购买方', org: '某物流公司', status: 'ACTIVE' },
      { id: 3, username: 'provider1', name: '李经理', role: '产品提供方', org: '智仓科技', status: 'ACTIVE' }
    ])

    const permissions = ref([
      { id: 1, name: '产品管理', code: 'product:manage', type: 'MENU' },
      { id: 2, name: '方案管理', code: 'solution:manage', type: 'MENU' },
      { id: 3, name: '试用管理', code: 'trial:manage', type: 'MENU' },
      { id: 4, name: '用户管理', code: 'user:manage', type: 'MENU' }
    ])

    const editRole = (role) => { console.log('编辑角色:', role) }
    const configurePermissions = (role) => { console.log('配置权限:', role) }
    const editUser = (user) => { console.log('编辑用户:', user) }
    const editPermission = (perm) => { console.log('编辑权限:', perm) }

    return { activeTab, roles, users, permissions, editRole, configurePermissions, editUser, editPermission }
  }
}
</script>

<style scoped>
.admin-roles { max-width: 1000px; margin: 0 auto; }
.page-header { margin-bottom: 24px; }
.page-header h1 { font-size: 24px; }
.tabs { display: flex; gap: 8px; margin-bottom: 20px; background: #fff; padding: 8px; border-radius: 12px; }
.tab { padding: 10px 20px; border: none; background: transparent; cursor: pointer; border-radius: 8px; }
.tab.active { background: #1890ff; color: #fff; }
.card { background: #fff; border-radius: 12px; padding: 24px; }
code { padding: 2px 6px; background: #f5f5f5; border-radius: 4px; font-size: 13px; }
.tag { padding: 4px 10px; border-radius: 4px; font-size: 12px; }
.tag-primary { background: #e6f7ff; color: #1890ff; }
.tag-success { background: #f6ffed; color: #52c41a; }
.tag-default { background: #f5f5f5; color: #666; }
.btn-sm { padding: 6px 12px; font-size: 12px; border-radius: 4px; border: 1px solid #d9d9d9; background: #fff; cursor: pointer; margin-right: 8px; }
</style>
